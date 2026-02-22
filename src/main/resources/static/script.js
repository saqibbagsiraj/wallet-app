console.log("JS loaded successfully");

// CREATE WALLET
function createWallet() {
    const username = document.getElementById("username").value;
    const mobile = document.getElementById("mobile").value;

    fetch(`/api/wallets?username=${username}&mobile=${mobile}`, {
        method: "POST"
    })
    .then(async res => {
        const text = await res.text();
        if (!res.ok) throw text;
        return text;
    })
    .then(() => {
        document.getElementById("createMsg").innerHTML =
            "<span class='success'>Wallet created successfully</span>";
        loadWallets();
    })
    .catch(err => {
        document.getElementById("createMsg").innerHTML =
            `<span class='error'>${err}</span>`;
    });
}

// LOAD ALL WALLETS
function loadWallets() {
    fetch("/api/wallets")
        .then(res => res.json())
        .then(data => {
            const table = document.getElementById("walletTable");
            table.innerHTML = "";

            data.forEach(w => {
                table.innerHTML += `
                    <tr>
                        <td>${w.id}</td>
                        <td>${w.username}</td>
                        <td>${w.mobile}</td>
                        <td>₹ ${w.balance}</td>
                       <td>
                           <button type="button" onclick="loadMoney(${w.id})">Load</button>
                           <button type="button" onclick="withdrawMoney(${w.id})">Withdraw</button>
                           <button type="button" onclick="deleteWallet(${w.id})">Delete</button>
                       </td>
                    </tr>
                `;
            });
        });
}
// TRANSFER MONEY
function transferMoney() {
    const fromMobile = document.getElementById("fromMobile").value;
    const toMobile = document.getElementById("toMobile").value;
    const amount = document.getElementById("amount").value;

    fetch("/api/transactions/transfer", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            fromMobile,
            toMobile,
            amount
        })
    })
    .then(async res => {
        const text = await res.text();
        if (!res.ok) throw text;
        return text;
    })
    .then(msg => {
        document.getElementById("transferMsg").innerHTML =
            `<span class='success'>${msg}</span>`;
        loadWallets();
    })
    .catch(err => {
        document.getElementById("transferMsg").innerHTML =
            `<span class='error'>${err}</span>`;
    });
}

// AUTO LOAD
function loadMoney(walletId) {
    const amount = prompt("Enter amount to load:");
    if (!amount || amount <= 0) return;

    fetch(`/api/wallets/${walletId}/load?amount=${amount}`, {
        method: "POST"
    })
    .then(() => loadWallets())
    .catch(err => alert(err));
}

function withdrawMoney(walletId) {
    const amount = prompt("Enter amount to withdraw:");
    if (!amount || amount <= 0) return;

    fetch(`/api/wallets/${walletId}/withdraw?amount=${amount}`, {
        method: "POST"
    })
    .then(() => loadWallets())
    .catch(err => alert(err));
}
loadWallets();

function deleteWallet(walletId) {
    if (!confirm("Are you sure you want to delete this wallet?")) return;

    fetch(`/api/wallets/${walletId}`, {
        method: "DELETE"
    })
    .then(async res => {
        const text = await res.text();
        if (!res.ok) throw text;
        alert(text);
        loadWallets();
    })
    .catch(err => alert(err));
}