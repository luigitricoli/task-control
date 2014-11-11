$(document).ready(function(){
    $('input[id ^= "optReportType"]').bind("click", function(evt) {
        prepareFiltersForReport($(this), evt);
    });
    
    $("#btSubmitReport").click(function(evt) {
        submitReport($(this), evt);
        evt.preventDefault();
    });
    
    $("#reportParamGroup-yearMonth").hide();
    $("#reportParamGroup-date").hide();
    $("#reportSubmit").hide();
    
    // Selects default date as today
    var dt = new Date();

    $("#cboReportMonth").val(dt.getMonth() + 1);
    $("#cboReportYear").val(dt.getFullYear());

    $("#txtReportDate").mask('00/00/0000');
    $("#txtReportDate").datepicker({
                        "dateFormat": "dd/mm/yy",
                        "showOn": "button",
    });

    updateMonthName();
    $("#cboReportMonth").click(function() {
        updateMonthName();
    })
});

function prepareFiltersForReport(obj, evt) {
    clearAlert();

    if(obj.val() == "taskList") {
        $("#reportParamGroup-yearMonth").show();
        $("#reportParamGroup-date").hide();
        $("#reportSubmit").show();
    } else if (obj.val() == "dailyActivities") {
        $("#reportParamGroup-yearMonth").hide();
        $("#reportParamGroup-date").show();
        $("#reportSubmit").show();
    } else {
        $("#reportParamGroup-yearMonth").hide();
        $("#reportParamGroup-date").hide();
        $("#reportSubmit").hide();
    }
}

function submitReport(obj, evt) {
    clearAlert();

    var selectedType = $('input[name="optReportType"]:checked').val();

    var url;
    if (selectedType == "taskList") {
        $.ajax({
            url: DOMAIN + "relatorios/listaTarefas",
            method: "get",
            data: $("#report-options-form").serialize(),
            success: function(data) {
                $("#report-area").html(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // TODO Improve this error handling
                alert("Erro ao requisitar relatorio: " + jqXHR.status);
            }
        });

    } else if (selectedType == "dailyActivities") {
        if ($("#txtReportDate").val() == "") {
            showAlert("Informe a data de pesquisa");
            return;
        }

        $.ajax({
            url: DOMAIN + "relatorios/atividadesDiarias",
            method: "get",
            data: $("#report-options-form").serialize(),
            success: function(data) {
                $("#report-area").html(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // TODO Improve this error handling
                alert("Erro ao requisitar relatorio: " + jqXHR.status + "\n" + jqXHR.responseText);
            }
        });
    } else {
        alert("ERROR: Unknown report type");
    }
}

function refreshTable(url) {
    $.ajax({
        url: url,
        method: "get",
        success: function(data) {
            $("#report-area").html(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // TODO Improve this error handling
            alert("Erro ao requisitar relatorio: " + jqXHR.status);
        }
    });    
}

function customizeTableLinks() {
    // Turn simple links into Ajax links
    $("th.sortable a, div.pagelinks a").each(function() {
        var url = $(this).attr("href");
        $(this).attr("href", "#");
        $(this).click(function(evt) {
            refreshTable(url);
            evt.preventDefault();
        });
    });
    
    // Export links should open in a new window/tab
    $(".exportlinks a").each(function() {
        $(this).attr("target", "_blank");
    });
}

function showAlert(message) {
    $(".alert").text(message).show();
}

function clearAlert() {
    $(".alert").text("").hide();
}

function updateMonthName() {
    var monthName = $("#cboReportMonth option").filter(":selected").text();
    $("#hidReportMonthName").val(monthName);
}