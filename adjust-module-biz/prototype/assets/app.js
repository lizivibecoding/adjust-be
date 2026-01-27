function showToast(message) {
  var toast = document.querySelector('.toast');
  if (!toast) {
    return;
  }
  toast.textContent = message;
  toast.style.display = 'block';
  clearTimeout(window.__toastTimer);
  window.__toastTimer = setTimeout(function () {
    toast.style.display = 'none';
  }, 1600);
}

function initTabbar() {
  var active = document.body.getAttribute('data-tab');
  if (!active) {
    return;
  }
  document.querySelectorAll('.tabbar a').forEach(function (item) {
    if (item.getAttribute('data-tab') === active) {
      item.classList.add('active');
    }
  });
}

function initTabs() {
  document.querySelectorAll('[data-tab-group]').forEach(function (group) {
    var items = group.querySelectorAll('[data-tab-item]');
    items.forEach(function (item) {
      item.addEventListener('click', function () {
        items.forEach(function (node) {
          node.classList.remove('active');
        });
        item.classList.add('active');
        var target = item.getAttribute('data-target');
        if (target) {
          document.querySelectorAll('[data-panel]').forEach(function (panel) {
            panel.classList.remove('active');
          });
          var active = document.querySelector('[data-panel=\"' + target + '\"]');
          if (active) {
            active.classList.add('active');
          }
        }
      });
    });
  });
}

document.addEventListener('DOMContentLoaded', function () {
  initTabbar();
  initTabs();
});
