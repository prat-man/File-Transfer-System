// file upload progress bar
$(function() {
	var pathbar = $('#pathbar');
	var browser = $('#browser');
	var progress = $('#fileUploadProgress');
	var progressVal = $('#fileUploadProgressValue');
	var uploadSubmit = $('#fileUploadSubmit');
	var flag = false;
	var task;

	$('#fileUploadForm').ajaxForm({
		beforeSend : function() {
			task = ping();
			
			pathbar.bind('click', function(e) { e.preventDefault();});
			browser.bind('click', function(e) { e.preventDefault();});
			pathbar.addClass('disabled');
			browser.addClass('disabled');
			progress.val(0);
			progressVal.html('0%');
			progress.css('display', 'inline-block');
			progressVal.css('display', 'inline-block');
			uploadSubmit.css('display', 'none');
			$('#fileUploadField').prop('disabled', true);
			$('#folderUploadForm *').prop('disabled', true);
			$('#createFolderForm *').prop('disabled', true);
		},
		uploadProgress : function(event, position, total, percentComplete) {
			if (flag || percentComplete == 100) {
				flag = true;
				progress.removeAttr('value');
				progressVal.html('Processing ...');
			}
			else {
				flag = false;
				progress.val(percentComplete / 100);
				progressVal.html(percentComplete + '%');
			}
		},
		complete : function(xhr) {
			clearInterval(task);
			
			if (xhr.status === 200) {
				alert("Upload Complete!");
			}
			else {
				alert("Upload Failed!");
			}
			browser.unbind('click');
			progress.css('display', 'none');
			progressVal.css('display', 'none');
			uploadSubmit.css('display', 'inline-block');
			$('#fileUploadField').prop('disabled', false);
			$('#folderUploadForm *').prop('disabled', false);
			$('#createFolderForm *').prop('disabled', false);
			location.reload();
		}
	});
});

// folder upload progress bar
$(function() {
	var pathbar = $('#pathbar');
	var browser = $('#browser');
	var progress = $('#folderUploadProgress');
	var progressVal = $('#folderUploadProgressValue');
	var uploadSubmit = $('#folderUploadSubmit');
	var flag = false;
	var task;

	$('#folderUploadForm').ajaxForm({
		beforeSend : function() {
			task = ping();
			
			pathbar.bind('click', function(e) { e.preventDefault();});
			browser.bind('click', function(e) { e.preventDefault();});
			pathbar.addClass('disabled');
			browser.addClass('disabled');
			progress.val(0);
			progressVal.html('0%');
			progress.css('display', 'inline-block');
			progressVal.css('display', 'inline-block');
			uploadSubmit.css('display', 'none');
			$('#folderUploadField').prop('disabled', true);
			$('#fileUploadForm *').prop('disabled', true);
			$('#createFolderForm *').prop('disabled', true);
		},
		uploadProgress : function(event, position, total, percentComplete) {
			if (flag || percentComplete == 100) {
				flag = true;
				progress.removeAttr('value');
				progressVal.html('Processing ...');
			}
			else {
				flag = false;
				progress.val(percentComplete / 100);
				progressVal.html(percentComplete + '%');
			}
		},
		complete : function(xhr) {
			clearInterval(task);
			
			if (xhr.status === 200) {
				alert("Upload Complete!");
			}
			else {
				alert("Upload Failed!");
			}
			browser.unbind('click');
			progress.css('display', 'none');
			progressVal.css('display', 'none');
			uploadSubmit.css('display', 'inline-block');
			$('#folderUploadField').prop('disabled', false);
			$('#fileUploadForm *').prop('disabled', false);
			$('#createFolderForm *').prop('disabled', false);
			location.reload();
		}
	});
});

// folder create
$(function(){
	$('#createFolderForm').bind('submit', function(event) {
		
		event.preventDefault();
		
		var form = $(this);
		
		$.ajax({
			url: '/createFolder',
			type: 'POST',
			data: form.serialize(),
			success: function(data) {
				location.reload();
			},
			error: function(data) {
				alert(data.responseJSON.message);
			}
		});
	});
});

// handle file names for folder upload
$(function(){
	$('#folderUploadField').bind('change', function(event) {
		var fileNames = "";
		var files = event.target.files;
		for (var i = 0; i < files.length; i++) {
			var path = files[i].webkitRelativePath;
			if (path === undefined) {
				path = "";
			}
			fileNames += path + ";";
		};
		$('#folderUploadForm #fileNames').val(fileNames);
	});
});

// folder or multi-file upload interface switch
$(function(){
	// currently all mobile devices are disabled, since they are not working properly
	if (isFolderUploadSupported() && !isMobileDevice()) {
		$('#folderUploadWarning').remove();
		
		$('#folderUploadField').removeAttr('multiple');
	} else {
		$('#folderUploadWarning').show();
		$('#folderUploadLabel').html('Select <b>multiple files</b> to upload');
		
		var element = $('#folderUploadField');
		var list = ['webkitdirectory', 'mozdirectory', 'msdirectory', 'odirectory', 'directory'];
		for (var i = 0; i < list.length; i++) {
			element.removeAttr(list[i]);
		}
	}
});

// check if folder upload is supported
function isFolderUploadSupported() {
	var element = document.createElement('input');
	element.type = 'file';
	var list = ['webkitdirectory', 'mozdirectory', 'msdirectory', 'odirectory', 'directory'];
	var flag = false;
	for (var i = 0; i < list.length; i++) {
		if (list[i] in element) {
			flag = true;
			break;
		}
	}
	return flag;
}

// check if mobile device
function isMobileDevice() {
	var check = false;
	(function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
	return check;
}

// file or folder delete
function deleteFile(fileName, filePath) {
	if (confirm("Are you sure you want to delete \"" + fileName + "\"?")) {		
		$.ajax({
			url: '/delete?path=' + filePath,
			type: 'DELETE',
			success: function(data) {
				location.reload();
			},
			error: function(data) {
				alert(data.responseJSON.message);
				location.reload();
			}
		});
	}
}

// back button reload
window.addEventListener("pageshow", function(event) {
	var historyTraversal = event.persisted
			|| (typeof window.performance != "undefined" && window.performance.navigation.type === 2);
	if (historyTraversal) {
		window.location.reload();
	}
});

// ping every 5 minutes to keep session alive
function ping() {
	return setInterval(function () {
		$.get("/ping", function(data) {});
	}, 1000 * 60 * 5);
}
