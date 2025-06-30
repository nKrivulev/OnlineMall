const API = "http://localhost:9000/api";

document.addEventListener("DOMContentLoaded", () => {
    loadNavbar();

    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const formData = new FormData(loginForm);
            const params = new URLSearchParams();
            for (const [key, value] of formData.entries()) {
                params.append(key, value);
            }

            const response = await fetch("/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: params,
                credentials: "include"
            });

            if (response.ok) {
                const userRes = await fetch(API + "/auth/me", {
                    credentials: "include"
                });

                if (userRes.ok) {
                    const userData = await userRes.json();
                    localStorage.setItem("user", JSON.stringify(userData));
                    window.location.href = "index.html";
                } else {
                    alert("Входът е успешен, но не можахме да заредим профила.");
                }
            } else {
                alert("Невалидни данни за вход.");
            }
        });
    }
});

async function loadNavbar() {
    const navbarContainer = document.getElementById("navbar-container");
    if (!navbarContainer) return;

    const res = await fetch("navbar.html");
    const html = await res.text();
    navbarContainer.innerHTML = html;

    setupNavbar();
}

function setupNavbar() {
    const user = JSON.parse(localStorage.getItem("user"));

    const loginLink = document.getElementById("login-link");
    const registerLink = document.getElementById("register-link");

    const userLinks = document.getElementById("user-links");
    const adminLink = document.getElementById("admin-link");
    const sellerLink = document.getElementById("seller-link");
    const ordersLink = document.getElementById("orders-link");
    const logoutButton = document.getElementById("logout-button");

    if (user) {
        if (loginLink) loginLink.style.display = "none";
        if (registerLink) registerLink.style.display = "none";

        if (userLinks) userLinks.style.display = "inline";

        // Покажи "Поръчки" за ВСЕКИ логнат потребител
        if (ordersLink) ordersLink.style.display = "inline";

        if (adminLink) adminLink.style.display = user.role === "ADMIN" ? "inline" : "none";
        if (sellerLink) sellerLink.style.display = user.role === "SELLER" ? "inline" : "none";

        if (logoutButton) {
            logoutButton.addEventListener("click", () => {
                fetch("/logout", {
                    method: "POST",
                    credentials: "include"
                }).finally(() => {
                    localStorage.removeItem("user");
                    window.location.href = "index.html";
                });
            });
        }
    } else {
        if (userLinks) userLinks.style.display = "none";
        if (loginLink) loginLink.style.display = "inline";
        if (registerLink) registerLink.style.display = "inline";
    }
    const createStoreLink = document.getElementById("create-store-link");
    if (createStoreLink) {
        createStoreLink.style.display = user.role === "SELLER" ? "inline" : "none";
    }
}
