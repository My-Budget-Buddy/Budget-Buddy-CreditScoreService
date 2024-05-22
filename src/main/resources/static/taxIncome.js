function calculateTaxableIncome() {
    var income = parseInt(document.getElementById('income').value);
    var dependents = parseInt(document.getElementById('dependents').value || 0);
    var k401 = parseInt(document.getElementById('401k').value || 0);
    var status = document.getElementById('status').value;

    var standardDeduction = (status === 'single') ? 13850 : 27700;

    var taxableIncome = income - (dependents * 1500) - k401 - standardDeduction;

    document.getElementById('result').innerText = `Final Taxable Income: $${taxableIncome}`;

    // Show button to return to index.html
    document.getElementById('returnButton').style.display = 'block';
}

function returnToIndex() {
    window.location.href = 'index.html'; // Redirect to index.html
}
