
/**
 * hashcode
 * author lijun
 */
function hashcode(str){
	var key = str || '';
	var h = 0;
	var off = 0;
	var len = key.length;
    for (var i = 0; i < len; i++)
    {
        h = 31 * h + key.charCodeAt(off++);
    }
    var result = h.toString();
    result = result.replace(/\.|\+/g,'');
    return result;
}
