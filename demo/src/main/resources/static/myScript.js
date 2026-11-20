const API_URL = "http://localhost:8080/sutik";
let editingSutiId = null;
// GET - Hallgatók betöltése
async function loadSutik() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok)
            throw new Error("Hiba! Nem látom a sütiket!!");
        const sutik = await response.json();
        displaySutik(sutik);
    } catch (error) {
        showMessage(error.message, "error");
    }
}
// Táblázat feltöltése
function displaySutik(sutik) {
    const tableBody = document.getElementById("studentTableBody");
    tableBody.innerHTML = "";
    sutik.forEach(suti => {
        const row = document.createElement("tr");
        const id = suti.id;
        row.innerHTML = `
<td>${suti.név}</td>
<td>${suti.tipus}</td>
<td>${suti.dijazott}</td>
<td>
<button
class="edit-btn"
onclick="openEditModal('${id}')">
Edit
</button>
<button
class="delete-btn"
onclick="deleteSuti('${id}')">
Delete
</button>
</td>
`;
        tableBody.appendChild(row);
    });
}
// POST - Új hallgató
function openCreateModal() {
    editingSutiId = null;
    document.getElementById("modalTitle").textContent = "Új süti";
    document.getElementById("studentForm").reset();
    document.getElementById("studentModal").style.display = "block";
}
// PUT - Hallgató módosítása
async function openEditModal(id) {
    try {
        const response = await fetch(API_URL);
        if (!response.ok)
            throw new Error("Nem találtam a sütiket!");
        const sutik = await response.json();
        const suti = sutik.find(item => String(item.id) === String(id));
        if (!suti)
            throw new Error("Nem található a süti!");
        editingSutiId = id;
        document.getElementById("modalTitle").textContent = "Süti szerkesztése";
        document.getElementById("nev").value = suti.név;
        document.getElementById("tipus").value = suti.tipus;
        document.getElementById("dijazott").checked = suti.dijazott;
        document.getElementById("studentModal").style.display = "block";
    } catch (error) {
        showMessage(error.message, "error");
    }
}
// DELETE - Hallgató törlése
async function deleteSuti(id) {
    const confirmed = confirm("Biztosan törli ezt a sütit?");
    if (!confirmed)
        return;
    try {
        const response = await fetch(
            `${API_URL}/${id}`,
            {
                method: "DELETE",
                headers: {"Content-Type":"application/x-www-form-urlencoded"}
            }
        );
        if (!response.ok) {
            throw new Error(`HTTP hiba: ${response.status}`);
        }
        showMessage("Süti sikeresen törölve!", "success");
        await loadSutik();
    } catch (error) {
        showMessage(error.message, "error");
    }
}
function closeModal() {
    document.getElementById("studentModal").style.display = "none";
    editingSutiId = null;
}
// Üzenet megjelenítése
function showMessage(text, type) {
    const message = document.getElementById("message");
    message.textContent = text;
    message.className = type;
    setTimeout(() => { message.className = ""; }, 3000);
}
window.onclick = function(event) {
    const modal = document.getElementById("studentModal");
    if (event.target === modal)
        closeModal();
};
function PostPut() {
// POST / PUT form elküldése
    document.getElementById("studentForm").addEventListener("submit", async function(event) {
        event.preventDefault();
        const nev = document.getElementById("nev").value;
        const tipus = document.getElementById("tipus").value;
        const dijazott = document.getElementById("dijazott").checked;

        const formData = {
            "név": nev,
            "tipus": tipus,
            "dijazott": dijazott
        };

        try {
            let response;
// PUT
            if (editingSutiId) {
                response = await fetch(
                    `${API_URL}/${editingSutiId}`,
                    {
                        method: "PUT",
                        headers: {"Content-Type": "application/json"},
                        body: JSON.stringify(formData)
                    }
                );
            }
// POST
            else {
                response = await fetch(
                    API_URL,
                    {
                        method: "POST",
                        headers: {"Content-Type": "application/json"},
                        body: JSON.stringify(formData)
                    }
                );
            }
            if (!response.ok) {
                throw new Error(`HTTP hiba: ${response.status}`);
            }
            closeModal();
            if (editingSutiId) {
                showMessage("Süti sikeresen szerkesztve!", "success" );
            } else {
                showMessage("Süti sikeresen létrehozva!", "success");
            }
            await loadSutik();
        } catch (error) {
            showMessage(error.message, "error");
        }
    });
}
window.onload = function() {
    PostPut();
    loadSutik();
}