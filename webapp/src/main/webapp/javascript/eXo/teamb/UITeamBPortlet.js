(function($, window, document) {
  var UITeamBPortlet = {
    id : 'UITeamBPortlet',

    init : function(id) {
      UITeamBPortlet.id = id;
      //
      $('.effect-hover').on('mouseover', function() { $(this).addClass('display-over');})
                        .on('mouseout', function() { $(this).removeClass('display-over');});
      var container = $('#uiTaskContainer');
      
      container.find('.taskTitle').css('max-width', function() { return ((container.width()-6)/3 - 65)});
    },
    initTooltip : function(id) {
      if(id != null) {
        $('#'+id).find('[rel=tooltip]').tooltip();
      }
    },
    initConfirm : function(id) {
	    var component = $('#'+id);
	    var confirms = component.find('.confirm');
	    
	    $.each(confirms, function(idx, element) {
	      var thizz = $(element);
	      if(thizz.hasAttr('id') == false) {
	        thizz.attr('id', id + 'Confirm' + idx);
	      }
	      var settings = {isMulti: false, message : ''};
	      if(thizz.hasAttr('data-confirm')) {
	        settings.message = thizz.attr('data-confirm');
	      } else {
	    	settings.message = thizz.html();
	      }
	      thizz.confirmation(settings);
	    });
     }
  }
  return UITeamBPortlet;
})(gj, window, document);
