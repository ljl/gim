(function($){

	jQuery.event.props.push("dataTransfer");
	var opts = {},
		default_opts = {
			url: '',
            ws: '',
			refresh: 1000,
			paramname: 'userfile',
			maxfiles: 25,
			maxfilesize: 5, // MBs
			data: {},
			drop: empty,
			dragEnter: empty,
			dragOver: empty,
			dragLeave: empty,
			docEnter: empty,
			docOver: empty,
			docLeave: empty,
			beforeEach: empty,
			afterAll: empty,
			rename: empty,
			error: function(err, file, i){alert(err);},
			uploadStarted: empty,
			uploadFinished: empty,
			progressUpdated: empty,
			speedUpdated: empty
		},
		errors = ["BrowserNotSupported", "TooManyFiles", "FileTooLarge"],
		doc_leave_timer,
		stop_loop = false,
		files_count = 0,
		files;
    var imageSocket;

	$.fn.filedrop = function(options) {
		opts = $.extend( {}, default_opts, options );
		imageSocket = opts.ws;
		this.bind('drop', drop).bind('dragenter', dragEnter).bind('dragover', dragOver).bind('dragleave', dragLeave);
		$(document).bind('drop', docDrop).bind('dragenter', docEnter).bind('dragover', docOver).bind('dragleave', docLeave);
        document.onpaste = paste;
	};

    function paste(e) {
        console.log("pasting", e);
        files = itemsToFileArray(e.clipboardData.items);
        if (files === null || files === undefined) {
            opts.error(errors[0]);
            return false;
        }
        files_count = files.length;
        upload();
        e.preventDefault();
        return false;
    }

    function itemsToFileArray(items) {
        var fileArray = new Array();
        $.each(items, function(i, val) {
            fileArray[i] = val.getAsFile();
        });
        return fileArray;
    }
     
	function drop(e) {
		opts.drop(e);
		files = e.dataTransfer.files;
		if (files === null || files === undefined) {
			opts.error(errors[0]);
			return false;
		}
		
		files_count = files.length;
		upload();
		e.preventDefault();
		return false;
	}
    
	function upload() {
		stop_loop = false;
		if (!files) {
			opts.error(errors[0]);
			return false;
		}
		var filesDone = 0,
			filesRejected = 0;
		
		if (files_count > opts.maxfiles) {
		    opts.error(errors[1]);
		    return false;
		}

		for (var i=0; i<files_count; i++) {
			if (stop_loop) return false;
			try {
				if (beforeEach(files[i]) != false) {
					if (i === files_count) return;
					var reader = new FileReader(),
						max_file_size = 1048576 * opts.maxfilesize;
						
					reader.index = i;
					if (files[i].size > max_file_size) {
						opts.error(errors[2], files[i], i);
						filesRejected++;
						continue;
					}
					
					reader.onloadend = send;
					reader.readAsBinaryString(files[i]);
				} else {
					filesRejected++;
				}
			} catch(err) {
				opts.error(errors[0]);
				return false;
			}
		}
	    
		function send(e) {
			// Sometimes the index is not attached to the
			// event object. Find it by size. Hack for sure.
			if (e.target.index == undefined) {
				e.target.index = getIndexBySize(e.total);
			}
            var file = files[e.target.index];

            imageSocket.send(file);
		}

	}
    
	function getIndexBySize(size) {
		for (var i=0; i < files_count; i++) {
			if (files[i].size == size) {
				return i;
			}
		}
		
		return undefined;
	}

	function beforeEach(file) {
		return opts.beforeEach(file);
	}
	
	function dragEnter(e) {
		clearTimeout(doc_leave_timer);
		e.preventDefault();
		opts.dragEnter(e);
	}
	
	function dragOver(e) {
		clearTimeout(doc_leave_timer);
		e.preventDefault();
		opts.docOver(e);
		opts.dragOver(e);
	}
	 
	function dragLeave(e) {
		clearTimeout(doc_leave_timer);
		opts.dragLeave(e);
		e.stopPropagation();
	}
	 
	function docDrop(e) {
		e.preventDefault();
		opts.docLeave(e);
		return false;
	}
	 
	function docEnter(e) {
		clearTimeout(doc_leave_timer);
		e.preventDefault();
		opts.docEnter(e);
		return false;
	}
	 
	function docOver(e) {
		clearTimeout(doc_leave_timer);
		e.preventDefault();
		opts.docOver(e);
		return false;
	}
	 
	function docLeave(e) {
		doc_leave_timer = setTimeout(function(){
			opts.docLeave(e);
		}, 200);
	}
	 
	function empty(){}

})(jQuery);