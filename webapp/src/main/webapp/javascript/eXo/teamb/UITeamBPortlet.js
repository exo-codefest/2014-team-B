(function($, window, document) {
  var UITeamBPortlet = {
    id : 'UITeamBPortlet',

    init : function(id) {
      UITeamBPortlet.id = id;
      //
      $('.effect-hover').on('mouseover', function() { $(this).addClass('display-over');})
                        .on('mouseout', function() { $(this).removeClass('display-over');});
    },
    initTooltip : function(id) {
      if(id != null) {
        $('#'+id).find('[rel=tooltip]').tooltip();
      }
    }
  }
  return UITeamBPortlet;
})(gj, window, document);
