$(function(){

	var dropbox = $('#dropbox'),
		message = $('.message', dropbox);
	
	dropbox.filedrop({
		paramname:'pic',
		
		maxfiles: 5,
    	maxfilesize: 5,
		url: dropbox.attr('data-websocket'),
		
    	error: function(err, file) {
			switch(err) {
				case 'BrowserNotSupported':
					showMessage('Your browser does not support HTML5 file uploads!');
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

function viewImage(i) {
    var template = '<li class="span3" style="opacity:0;" id="i-'+ i.entry.id +'"><div class="thumbnail"><img src="' + i.imageUrl + '" /></div></li>';

    $('ul.thumbnails').prepend(template);
    setTimeout(function() {
        $("#i-" + i.entry.id ).addClass('in');
    }, 1200);
}

