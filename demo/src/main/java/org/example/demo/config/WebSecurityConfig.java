package org.example.demo.config;
import org.example.demo.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
@Configuration
public class WebSecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    // @Bean:
// A @Bean utasítja a Springet, hogy hozza létre, konfigurálja és mentse el az objektumot a
// saját memóriájába (Spring IoC (Inversion of Control) konténer), hogy azt később az alkalmazás
// más pontjain automatikusan fel lehessen használni (Dependency Injection).
// Itt a @Bean hatására a Spring készít egy PasswordEncoder objektumot és elmenti
// a memóriájába (IoC (Inversion of Control) konténer), hogy máshol be lehessen injektálni
// (Dependency Injection)
// A Controller2 konstruktorában injektáljuk be:
// public Controller2(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    @Bean
// Jelszó titkosításának beállítása:
// megmondja a Spring Security számára, hogy milyen algoritmussal kezelje a jelszavakat.
// itt BCrypt algoritmussal
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // a Spring készít egy DaoAuthenticationProvider objektumot és elmenti a memóriájába
// (IoC (Inversion of Control) konténer) a Dependency Injection-hoz
// A DaoAuthenticationProvider objektumot a Spring Security keretrendszer injektálja be saját
// magának: nem mi fogjuk meghívni vagy beilleszteni más osztályokba, hanem a Spring Security
// belső motorja keresi meg és használja fel az autentikációs folyamat során.
    @Bean
// A felhasználó betöltésének beállítása
// A SecurityConfig összeköti a Spring Security-t a saját CustomUserDetailsService osztállyal.
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    // Készít egy AuthenticationSuccessHandler objektumot, elmenti a memóriájába
// (IoC (Inversion of Control) konténer) a Dependency Injection-hoz.
// Ebben a fájlban a securityFilterChain metódusban injektálja be (lásd lentebb):
// .successHandler(authenticationSuccessHandler())
    @Bean
// Ha user bejelentkezik, akkor irányítsa át a /user útvonalra.
// Ha admin bejelentkezik, akkor irányítsa át az /admin útvonalra.
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/user");
            }
        };
    }
    // A SecurityFilterChain központi eleme a Spring Security-nek.
// a SecurityFilterChain magát a teljes biztonsági védvonalat jelenti, amin minden egyes beérkező
// HTTP kérésnek át kell mennie.
// Ezt az objektumot a Spring keretrendszer az alkalmazás szűrőinek legalapvetőbb rétegébe, a
// DelegatingFilterProxy-ba injektálja be.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authenticationProvider(authenticationProvider())
// URL-ek védelme: megmondja, hogy ki melyik oldalt nézheti meg.
                .authorizeHttpRequests(auth -> auth
// Ezeket mindenki megnézhati: Látogató, User, Admin
                                .requestMatchers("/", "/register", "/login", "/css/**", "/js/**").permitAll()
// Ezt csak a User és az Admin nézheti meg:
                                .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
// Ezt csak az Admin nézheti meg:
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
// A form-alapú (űrlapos) bejelentkezést konfigurálja és szabja testre a Spring Security-ben.
// Engedélyezi és bekapcsolja az űrlapos bejelentkezést:
                .formLogin(form -> form
// Megadja a saját bejelentkezési oldalad elérési útját.: /login
                                .loginPage("/login")
// Beállítja, hogy sikeres bejelentkezés után ne a Spring alapértelmezett átirányítása fusson le
// (ami általában a főoldal vagy az utoljára megtekintett lap), hanem az a szerepkör-alapú
// átirányítási logika, amit az előző lépésben az authenticationSuccessHandler() metódusban
// megírtunk (vagyis az adminokat az /admin, a többieket a /user oldalra küldi).
                                .successHandler(authenticationSuccessHandler())
// Biztosítja, hogy bárki (be nem jelentkezett vendégek is) elérhesse a login oldalt és a hozzá kapcsolódó
// feldolgozó URL-t.
                                .permitAll()
                )
// Logout beállítása: kijelentkezés után hova kerüljön a felhasználó:
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
// A biztonsági hibák kezelését (Exception Handling) szabályozza. Azt határozza meg, hogy mi történjen,
// ha egy látogató bejelentkezés nélkül próbál megtekinteni egy védett oldalt, vagy ha nincs
// megfelelő jogosultsága (szerepköre) egy oldalhoz.
// A kód két fő részre oszlik: az AuthenticationEntryPoint-okra (be nem jelentkezett felhasználók) és
// az AccessDeniedHandler-re (már bejelentkezett, de nem jogosult felhasználók).
                .exceptionHandling(exception -> exception
// A be nem jelentkezett felhasználók kezelése: mi történjen, ha egy névtelen/vendég látogató olyan
// oldalt akar megnyitni, amihez be kellene jelentkezni.
// Ha a vendég felhasználó a /user oldalt próbálja elérni (request.getRequestURI().equals("/user")), a
// Spring nem engedi be, hanem átirányítja a /login (bejelentkezési) oldalra.
                                .defaultAuthenticationEntryPointFor(
                                        (request, response, authException) ->
                                                response.sendRedirect("/login"), request ->
                                                request.getRequestURI().equals("/user")
                                )
// Ha a vendég felhasználó az /admin oldalt próbálja elérni, a kód átirányítja őt a főoldalra (/)
                                .defaultAuthenticationEntryPointFor(
                                        (request, response, authException) ->
                                                response.sendRedirect("/"),request -> request.getRequestURI().equals("/admin")
                                )
// A tiltott hozzáférés kezelése: Ez a rész akkor lép életbe, ha a felhasználó már be van jelentkezve, de
// olyan oldalra kattint, amihez nincs meg a szükséges szerepköre (például egy egyszerű USER
// szerepkörű tag megpróbál belépni az /admin felületre): irányítsa át a főoldalra (/)
                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) -> response.sendRedirect("/")
                                )
                );
        return http.build();
    }
}