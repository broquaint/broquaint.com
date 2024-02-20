function createRepoItem(repo) {
    /*  Oh for non-sucky templating :/
        <li>
          <a title='Project name' href=""></a> &sdot; <span class=lang title='Main language'></span> &sdot; <span class=ymd title='Pushed at'></span>
          <p></p>
        </li>
    */
    return jQuery('<li>').append(
        jQuery('<a>', { href: repo.html_url, title: 'Project name', text: repo.full_name }),
        ' &sdot; ',
        jQuery('<span>', { class: 'lang', title: 'Main' + ' language', text: repo.language }),
        ' &sdot; ',
        jQuery('<span>', { class: 'ymd', title: 'Pushed at', text: repo.pushed_at }),
        jQuery('<p>', { text: repo.description })
    );
}

function handleReposJSON(result) {
  jQuery('#projects ul')
    .empty()
    .append(jQuery.map(result.repos, createRepoItem));
}

// What's the point of having your own website if you can't have needless animations?
function startRotateSection() {
    var hr = jQuery('#projects hr').get(0);
    hr.style.setProperty('--hr-rot', '0deg');
    hr.dataset['rotating'] = 'spin';
    function spin() {
	if(hr.dataset['rotating'] === 'spin') {
	    var rot = hr.style.getPropertyValue('--hr-rot');
	    var nextDeg = (Number(rot.replace(/deg/, '')) + 1) % 360;
	    hr.style.setProperty('--hr-rot', `${nextDeg}deg`);
	    window.requestAnimationFrame(spin);
	}
    }
    window.requestAnimationFrame(spin);
}

function stopRotateSection() {
    var hr = jQuery('#projects hr').get(0);
    hr.dataset['rotating'] = 'stop';
    hr.style.setProperty('--hr-rot', '0deg');
}

jQuery(function() {
  // If the repos weren't already fetched, serve the page then fetch the repos.
  if(jQuery('#projects ul li').length === 0)
      setTimeout(() => jQuery.getJSON('/repos.json', {}, handleReposJSON), 60*1000);

  jQuery('#projects hr').hover(startRotateSection, stopRotateSection)
});

