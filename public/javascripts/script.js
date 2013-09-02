$(function(){


	var dropbox = $('#dropbox'),
		message = $('.message', dropbox);

    var webSocket = new WebSocket(dropbox.attr('data-websocket'));

    webSocket.onmessage = function(e) {
        var result = jQuery.parseJSON(e.data);
        console.log(result);
        var type = result.type;
        switch(type) {
            case "image":
                imageUploaded(result);
                break;
            default:
                break;
        }
    };

	dropbox.filedrop({
		paramname:'pic',
		
		maxfiles: 5,
    	maxfilesize: 5,
		ws: webSocket,
		
    	error: function(err, file) {
			switch(err) {
				case 'BrowserNotSupported':
					notify('Your browser does not support HTML5 file uploads!');
					break;
				case 'TooManyFiles':
					alert('Too many files! Please select 5 at most! (configurable)');
					break;
				case 'FileTooLarge':
					alert(file.name+' is too large! Please upload files up to 2mb (configurable).');
					break;
				default:
					break;
			}
		},

		// Called before each upload is started
		beforeEach: function(file){
			if(!file.type.match(/^image\//)){
				alert('Only images are allowed!');
				return false;
			}
		}
    	 
	});

});

// When an image is uploaded
function imageUploaded(result) {
    var message = result.entry.name + " created at " + result.entry.created;
    var id = result.entry.id;
    notify(id, message);
    viewImage(result);
}

// Sends a notification message
function notify(id, message) {
        var template = '<div class="alert alert-success" id="n-' + id + '">[msg]</div>';

        $('#notify').append(template.replace('[msg]', message));
        setTimeout(function() {
            $("#n-" + id ).addClass('in');
            setTimeout(function() {
                $("#n-" + id ).removeClass('in');
                setTimeout(function() {
                    $("#n-" + id ).remove();
                }, 4000);
            }, 4000);
        }, 300);

    }

// Displays an image
function viewImage(i) {
    var template = '<li class="span3" style="opacity:0;" id="i-'+ i.entry.id +'"><div class="thumbnail"><img src="' + i.imageUrl + '" /></div></li>';
    var alreadyExists = $('#i-' + i.entry.id ).exists();
    if (!alreadyExists) {
        $('ul.thumbnails').prepend(template);
        setTimeout(function() {
            $("#i-" + i.entry.id ).addClass('in');
        }, 1200);
    }
}

// Returns true if an element exists
jQuery.fn.exists = function(){return this.length>0;};
