var NUMBER_REGEXP = /(\d+)/g;
var DEFAULT_DATE_SEPARATOR = "/";
var DEFAULT_HOUR_SEPARATOR = ":";

function today(){
    var d = new Date();
    return "{0}/{1}/{2} {3}:{4}:{5}".format([d.getDate(), d.getMonth()+1, d.getFullYear(), d.getHours(), d.getMinutes(), d.getSeconds()]);;
}

function BrazilianDate(date){
    this.sDate = date || today();

    var tmp = this.sDate.match(NUMBER_REGEXP);

    if(tmp.length !== 3 && tmp.length !== 6) {
        throw "The date parameter is not one of these formats: [dd/MM/yy] or [dd/MM/yyyy] or [dd/MM/yy HH:mm:ss] or [dd/MM/yyyy HH:mm:ss]"
    }

    this.day = tmp[0];
    this.month = tmp[1];
    this.year = 2 === tmp[2].length ? "20{0}".format([tmp[2]]) : tmp[2];

    this.hour = tmp[3] || 0;
    this.minute = tmp[4] || 0;
    this.second = tmp[5] || 0;
}

BrazilianDate.prototype.compare = function(other) {
        var compare = "{0}{1}{2}{3}{4}{5}";
        var thisCompare = compare.format([this.year, this.month, this.day, this.hour, this.minute, this.second])
        var otherCompare = compare.format([other.year, other.month, other.day, other.hour, other.minute, other.second])

        if(thisCompare < otherCompare){
            return -1;
        }
        if(thisCompare > otherCompare){
            return 1;
        }

        return 0;
}