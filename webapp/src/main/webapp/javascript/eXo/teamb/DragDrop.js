(function($, window, document) {

  var DragDrop = {
    dragObject : null,
    targetClass : [],
    component : null,
    allTargetClass : ["open-items", "inprogress-items", "done-items"],

    init : function(compid) {
      DragDrop.component = $('#'+compid);
      console.log('init drop ' + compid);
      DragDrop.component.find('li.task-item').off('mousedown').on('mousedown', DragDrop.attach);

      DragDrop.disableSelection(DragDrop.component);
    },

    attach : function(evt) {
      evt.cancelBubble = true;
      gj.event.fix(evt).stopPropagation();
      evt.preventDefault();
      
      DragDrop.rootNode = this;
      DragDrop.hided = false;

      DragDrop.dragObject = $('<li class="dropItem"></li>');
      DragDrop.source = $(this);
      DragDrop.dragObject.text(DragDrop.source.find('.taskTitle:first').text())
            .attr('data-id', DragDrop.source.data('id'));
      DragDrop.dragObject.css({
        'border' : 'solid 1px #cfcfcf',
        'line-height' : '24px',
        'position' : 'absolute',
        'width' : DragDrop.source.width()- 50,
        'white-space' : 'nowrap',
        'overflow': 'hidden',
        'opacity': '0.85',
        'border-radius': '5px',
        'padding': '2px 5px',
        'background' : '#fff'
      });
      var ul = $('#UIPortalApplication').find('.dropUl:first');
      if(ul.length == 0) {
         ul = $('<ul class="dropUl"></ul>');
         $('#UIPortalApplication').append(ul);
      }
      ul.html('').append(DragDrop.dragObject);

      DragDrop.mousePos = {
        x : evt.clientX,
        y : evt.clientY
      };
      var parent = $(this).parents('ul:first');
      DragDrop.targetClass = [];
      for(var i = 0; i < DragDrop.allTargetClass.length; ++i) {
        if(!parent.hasClass(DragDrop.allTargetClass[i])) {
          DragDrop.targetClass.push(DragDrop.allTargetClass[i]);
        }
      }
      //
      DragDrop.setup();
    },

    setup : function() {
      if($.browser.msie) {
        $(document).on('dragstart', function(e){
          return false;
        });
      }
      $(document.body).on('mouseup', DragDrop.onDrop);
      $(document.body).on('mousemove', DragDrop.onDrag);
    },

    onDrag : function(evt) {
			if (DragDrop.source && DragDrop.source.length > 0) {
				DragDrop.source.hide();
			}
      if (DragDrop.dragObject && DragDrop.dragObject.length > 0) {
        DragDrop.dragObject.css({
          'left' : (evt.pageX + 2) + 'px',
          'top' : (evt.pageY + 2) + 'px'
        });
        var target = DragDrop.findTarget(evt);
        DragDrop.dragCallback(target);
      }
    },

    dragCallback : function(target) {
      DragDrop.component.find('li.tmpItem').hide();
      if(target) {
        var ul = $(target);
        ul.find('li.tmpItem:first').show();
      }
    },

    onDrop : function(evt) {
      evt = evt || window.event;
      //
      
      if(DragDrop.source && DragDrop.source.length > 0) {
        DragDrop.source.show(); 
      }
      
      DragDrop.isMoved = true;
      if (DragDrop.mousePos.x == evt.clientX && DragDrop.mousePos.y == evt.clientY) {
        DragDrop.isMoved = false;
      }
      if (DragDrop.dragObject && DragDrop.dragObject.length > 0) {
        var target = DragDrop.findTarget(evt);
        DragDrop.dropCallback(target);
      }
      DragDrop.endDrop();
    },

    dropCallback : function(target) {
      DragDrop.component.find('li.tmpItem').hide();
      
      if(target) {
        var ul = $(target);
        ul.append(DragDrop.source);
        //
        var params = '&objectId=' + window.decodeURI(DragDrop.source.attr('data-id'));
        params += '&taskstatus=' + ul.attr('data-action-type');
        var action = ul.attr('data-action').replace('javascript:', '');
        action = action.replace('ajaxRequest=true', 'ajaxRequest=true' + params);
        $.globalEval(action);
      }
    },

    endDrop : function() {
      delete DragDrop.dragObject;
      delete DragDrop.targetClass;
      delete DragDrop.hided;
      delete DragDrop.rootNode;
      delete DragDrop.source;
      $('#UIPortalApplication  > ul.dropUl').remove();
      
      if($.browser.msie) {
        $(document).off('dragstart');
      }
      $(document.body).off('mousemove', DragDrop.onDrag);
      $(document.body).off('mouseup', DragDrop.onDrop);
    },
    
    findTarget : function(evt) {
      var targetClass = DragDrop.targetClass;
      if (targetClass) {
        var targetEvt = $(DragDrop.getEventTarget(evt));
        for(var i = 0; i < targetClass.length; ++i) {
          var target = DragDrop.getEventTargetByClass(targetEvt, targetClass[i]);
          if (target) {
            return target;
          }
        }
      }
      return null;
    },
    getEventTarget : function(evt) {
      var evt = evt || window.event;
      var target = evt.target || evt.srcElement;
      if (target.nodeType == 3) { // check textNode
        target = target.parentNode;
      }
      return target;
    },
    getEventTargetByClass : function(targetEvt, className) {
      if (targetEvt.hasClass(className)) {
        return targetEvt[0];
      } else {
        var target = targetEvt.find('.' + className + ':first');
        if(target.length <= 0) {
          target = targetEvt.parents('.' + className + ':first');
        }
        return (target.length > 0) ? target[0] : null;
      }
    },

    disableSelection : function(jelm) {
      jelm.attr('unselectable', 'on').css('user-select', 'none').on('selectstart', function() {
        return false;
      });
      jelm.on('contextmenu', function() {
        return false;
      });
    }
  };

  return DragDrop;
})(gj, window, document);
