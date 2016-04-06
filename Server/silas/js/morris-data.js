
$(function() {

    var areachart = Morris.Area({
        element: 'morris-area-chart',
        xkey: 'glucose_test_date',
        ykeys: ['glucose_level'],
        labels: ['glucose_level'],
        pointSize: 2,
        hideHover: 'auto',
        resize: true
    });

    areachart.setData(dataGlucoseJson);

    var barchart = Morris.Bar({
        element: 'morris-bar-chart',
        xkey: 'blood_test_date',
        ykeys: ['haemoglobin_count', 'cholesterol', 'ahdl_cholesterol', 'ldl_cholesterol'],
        labels: ['haemoglobin_count', 'cholesterol', 'ahdl_cholesterol', 'ldl_cholesterol'],
        hideHover: 'auto',
        resize: true
    });
    barchart.setData(dataBloodJson);

});
