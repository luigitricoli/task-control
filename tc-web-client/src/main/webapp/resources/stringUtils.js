String.prototype.format = function (args) {
    var regex = /{-?[0-9]+}/g;
    var str = this;
    return str.replace(regex, function(item) {
        var intVal = parseInt(item.substring(1, item.length - 1));
        var replace;
        if (intVal >= 0) {
            replace = args[intVal];
        } else {
            replace = "";
        }
        return replace;
    });
};