function calculateTax() {
    var taxableIncome = document.getElementById('income').value;
    var status = document.getElementById('status').value;

    // Create a new FormData object
    var formData = new FormData();
    formData.append('taxableIncome', taxableIncome);

    // Send POST request to backend endpoint
    fetch('/calculate/' + status, {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        // Parse the response as a floating-point number
        var calculatedTax = parseFloat(data);
        
        // Format the calculated tax with 2 decimal points
        var formattedTax = calculatedTax.toFixed(2);

        document.getElementById('result').innerText = `Calculated Tax: $${formattedTax}`;
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
