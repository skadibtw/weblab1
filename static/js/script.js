function showToast(message) {
    const toast = document.getElementById("toast");

    toast.textContent = message;

    toast.classList.add("show");

    setTimeout(function() {
        toast.classList.remove("show");
        toast.textContent = "";
    }, 3000);
}

document.addEventListener("DOMContentLoaded", () => {
    loadResults();
document.getElementById("submit").addEventListener("click", function(event) {
    event.preventDefault();

    // Get X
    const xSelect = document.querySelector('input[name="x-value"]:checked');
    if (!xSelect) {
        showToast("Выберите значение X.");
        return;
    }

    // Get Y
    const yInput = document.getElementById("y-input").value.replace(",", ".");

    // Get R
    const rSelect = document.querySelector('input[name="r-value"]:checked');
    if (!rSelect) {
        showToast("Выберите значение R.");
        return;
    }

    // Validate Y
    if (!isValidY(yInput)) {
        showToast("Неверное значение Y. Оно должно быть числом от -3 до 3, а число знаков после запятой не должно превышать 10");
        return;
    }

    const x = parseFloat(xSelect.value);
    const y = parseFloat(yInput);
    const r = parseFloat(rSelect.value);

    // generate data
    const data = { x: x, y: y, r: r };

    // send post-request on fastcgi backend
    fetch("http://localhost:8080/fcgi-bin/main.jar", {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(resp => {
            if (!resp.ok) {
                console.log('Ошибка ответа от сервера...');
                return resp.text().then(text => { throw new Error(text) });
            }
            return resp.json(); // turn answ to json
        })
        .then(result => {
            saveResults(x, y, r, JSON.parse(result.response).hit, result.currentTime, result.elapsedTime);
            addResultToTable(x, y, r, JSON.parse(result.response).hit, result.currentTime, result.elapsedTime);
        })
        .catch(error => {
            console.error("Произошла ошибка:", error);
        });

});

    document.getElementById("clearResults").addEventListener("click", function() {
        clearResults();
        document.getElementById("result-body").innerHTML = ""; // Очистить таблицу
        showToast("Результаты очищены.");
    });

    function isValidY(value) {
    const y = parseFloat(value);
    return !isNaN(y) && y >= -3 && y <= 3;
}



//get results from response and add to table
function addResultToTable(x, y, r, hit, currentTime, elapsedTime) {

    const resultBody = document.getElementById("result-body");
    const newRow = document.createElement("tr");

    const xCell = document.createElement("td");
    xCell.textContent = x;

    const yCell = document.createElement("td");
    yCell.textContent = y;

    const rCell = document.createElement("td");
    rCell.textContent = r;

    const resultCell = document.createElement("td");
    resultCell.textContent = hit ? "Hit" : "Miss";

    const currentTimeCell = document.createElement("td");
    currentTimeCell.textContent = currentTime;

    const elapsedTimeCell = document.createElement("td");
    elapsedTimeCell.textContent = elapsedTime + " ms";

    newRow.appendChild(xCell);
    newRow.appendChild(yCell);
    newRow.appendChild(rCell);
    newRow.appendChild(resultCell);
    newRow.appendChild(currentTimeCell);
    newRow.appendChild(elapsedTimeCell);

    resultBody.appendChild(newRow);
}
    function saveResults(x, y, r, hit, currentTime, elapsedTime) {
        const storedResults = JSON.parse(localStorage.getItem('results')) || [];
        storedResults.push({ x, y, r, hit, currentTime, elapsedTime });
        localStorage.setItem('results', JSON.stringify(storedResults));
    }

    function loadResults() {
        const storedResults = JSON.parse(localStorage.getItem('results'));
        if (storedResults) {
            storedResults.forEach(result => {
                addResultToTable(result.x, result.y, result.r, result.hit, result.currentTime, result.elapsedTime);
            });
        }
    }

    function clearResults() {
        localStorage.removeItem('results'); // Удаляем результаты из localStorage
        document.getElementById("result-body").innerHTML = ""; // Очищаем таблицу
    }});

