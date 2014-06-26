(function($, window, document) {
  var UITeamBPortlet = {
    id : 'UITeamBPortlet',

    init : function(id) {
      UITeamBPortlet.id = id;
    },
    initTooltip : function(id) {
      if(id != null) {
        $('#'+id).find('[rel=tooltip]').tooltip();
      }
    }
  }
  return UITeamBPortlet;
})(gj, window, document);
