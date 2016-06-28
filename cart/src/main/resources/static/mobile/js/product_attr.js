var globaldata;
var formatlist;
var productImgList;

function popDetail(node){
	var buynode = $(node).parent().next();
	var pnode = $(node).closest(".product_box");
	var productUrl = pnode.find(".product_url:eq(0)").val();
	var thelistingid = pnode.find(".thelistingid:eq(0)").val();
	buynode.addClass("show_y");
	$.ajax({
		url: "/showproduct",
		data:{
			"producturl": productUrl
		},
		type: "get",
		dataType: "json",
		success:function(data){
			var plist = data.data.pdbList;
			globaldata = plist;
			//当前listingid
			
			paintPanel(buynode, thelistingid, plist);
		}
	});
}

//重画面板
function paintPanel(buynode, listing, plist){
	var attrlist = {};
	var typelist = {};
	var datalist = {};	//listingid对应的数据
	var imglist = {};
	for(var i=0;i<plist.length;i++){
		//组合产品拥有的属性的集合
		attrlist[plist[i].listingId] = plist[i].attributeMap;
		datalist[plist[i].listingId] = plist[i];
		for(var ii=0;ii<plist[i].imgList.length;ii++){
			if(plist[i].imgList[ii].isMain==true){
				imglist[plist[i].listingId] = plist[i].imgList[ii].imgUrl;
				break;
			}
		}
		//组合属性的集合
		if(plist[i].attributeMap){
			for(var a in plist[i].attributeMap){
				if(a in typelist){
					typelist[a].push(plist[i].attributeMap[a]);	
				}else{
					typelist[a] = [];
					typelist[a].push(plist[i].attributeMap[a]);
				}
			}
		}
	}

	formatlist = datalist;
	//图片list
	productImgList = imglist;
	var storageContent = buynode.find(".storage_content:eq(0)");
	getStorage(listing, datalist, storageContent);
	
	//当前属性
	var selectedLis = attrlist[listing];
	
	//绘制面板
	var html = '';
	for(var a in typelist){
		html += '<dd>'
        +'<span class="attr_name">'+a+':</span>'
        +'<span class="attr_val">';
		var arr = filterAttr(a,selectedLis,attrlist);
        for(var b=0;b<typelist[a].length;b++){
        	html += '<i class="'+(arr[typelist[a][b]]==listing?'current':'')+'" listing="'+arr[typelist[a][b]]+'" onclick="attrclick(this);" >'+typelist[a][b]+'</i>';
        }
        html += '</span></dd>';
	}
	var attrContent = buynode.find(".attrContent:eq(0)");
	attrContent.html(html);
}

function attrclick(node){
	var jnode = $(node);
	if(jnode.hasClass("current")){
		return ;
	}
	if(globaldata!=null){
		var buynode = jnode.closest(".buyPopBox");
		var box = buynode.find(".attrContent:eq(0)");
		var lis = jnode.attr("listing");
		var lisbox = buynode.closest(".product_box").find(".thelistingid:eq(0)");
		lisbox.val(lis);
		paintPanel(buynode,lis,globaldata);
		//改变信息
		changeInfo(lis,formatlist,buynode);
	}
}

//改变信息
function changeInfo(lis,formatlist,buynode){
	//当前选中的仓库
	var storageName = buynode.find(".storage_content .current").html();
	if(productImgList!=null){
		var img = $("#theurlprefix").val() + "/" + productImgList[lis];
		buynode.find(".theimg").attr("src", img);
	}
	var mainurl = $("#themainurl").val();
	var title = formatlist[lis].title;
	var url = formatlist[lis].url;
	var nowprice = formatlist[lis].whouse[storageName].nowprice;
	var oldprice = formatlist[lis].whouse[storageName].origprice;
	buynode.find(".theurl:eq(1)").html(title);
	buynode.find(".theurl").attr("href",mainurl+"/"+url+".html");
	buynode.find(".newprice").html(nowprice);
	buynode.find(".oldprice").html(oldprice);
	var isdiscount = parseFloat(oldprice) - parseFloat(nowprice);
	if(isdiscount>0){
		var dis = (isdiscount/parseFloat(oldprice))*100;
		buynode.find(".off").html(dis.toFixed(0)).show();
		buynode.find(".original_price").show();
	}else{
		buynode.find(".off").hide();
		buynode.find(".original_price").hide();
	}

}

//过滤每行属性
function filterAttr(nowattr,selectdata,data){
	var arr = {};
	$.each(data, function(i, item){
		var isfuhe = true;
		$.each(item, function(key, value){
			if(key!=nowattr && selectdata[key]!=value){
				isfuhe = false;
			}
		});
		if(isfuhe){
			var nt = item[nowattr];
			arr[nt] = i;
		}
	});
	return arr;
}

//显示仓库
function getStorage(listing, datalist, storageContent){
	var hisStorageid = storageContent.closest(".product_box").find(".hisStorageid:eq(0)").val();
	hisStorageid = parseInt(hisStorageid);
	var slist = datalist[listing].whouse;
	var html = '';
	var ishaveStorageid = false;
	for(var a in slist){
		if(slist[a].depotId==hisStorageid){
			ishaveStorageid = true;
		}
		html += '<i onclick="tapStorage(this)" storageid="'+slist[a].depotId+'" class="'+(slist[a].depotId==hisStorageid?'current':'')+'">'+a+'</i>';
	}
	storageContent.html(html);
	//如果不存在这个storageid，就取第一个
	if(ishaveStorageid==false){
		storageContent.find("i:eq(0)").click();
	}
}

function tapStorage(node){
	var thenode = $(node);
	var name = thenode.html();
	var pnode = thenode.closest(".product_box");
	var thesid = thenode.attr("storageid");
	pnode.find(".hisStorageid:eq(0)").val(thesid);
	var lis = pnode.find(".thelistingid:eq(0)").val();
	if(formatlist!=null){
		var price = formatlist[lis].whouse[name].nowprice;
		if(price){
			thenode.closest(".buyPopBox").find(".newprice:eq(0)").html(price);
			thenode.siblings().removeClass("current");
			thenode.addClass("current");
		}
		
	}
}

function addCart(node){
	var pnode = $(node).closest(".product_box");
	var lis = pnode.find(".thelistingid:eq(0)").val();
	var sid = pnode.find(".hisStorageid:eq(0)").val();
	var num = pnode.find(".qty_txt:eq(0)").val();
	
	var list = [];
	var map = {};
	map['clistingid'] = lis;
	map['qty'] = num;
	map['storageid'] = sid;
	list[0] = map;
	var dd = $.toJSON(list);
	var url = "/savecartitem";
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		//contentType: "application/json",
		data: {"data": dd},
		success: function(data){
			if(data.result=="success"){
				popError("Add to cart successful!");
			}
		}
	});
	
}