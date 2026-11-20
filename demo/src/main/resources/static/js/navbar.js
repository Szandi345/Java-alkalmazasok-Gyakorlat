document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.getElementById("sidebar");
    const wrapper = document.getElementById("wrapper");
    if (!sidebar || !wrapper) return;
    // Az Editorial sablon mobilos töréspontja
    const mobile = window.matchMedia("(max-width: 1280px)");
    function updateSidebar() {
        if (mobile.matches) {
            sidebar.classList.add("inactive");
        } else {
            sidebar.classList.remove("inactive");
        }
    }
    updateSidebar();
    mobile.addEventListener("change", updateSidebar);
    // Oldalsáv nyitó és záró gomb
    const toggle = document.createElement("a");
    toggle.href = "#sidebar";
    toggle.className = "toggle";
    toggle.setAttribute("aria-label", "Menü nyitása vagy zárása");
    document.getElementById("main").appendChild(toggle);
    toggle.addEventListener("click", function (event) {
        event.preventDefault();
        sidebar.classList.toggle("inactive");
    });
    // Forex almenü
    document.querySelectorAll("#menu .opener")
        .forEach(function (opener) {
            opener.addEventListener("click", function (event) {
                event.preventDefault();
                opener.classList.toggle("active");
            });
        });
});
