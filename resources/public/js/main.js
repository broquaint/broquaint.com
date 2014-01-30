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
        jQuery('<span>', { className: 'lang', title: 'Main' + ' language', text: repo.language }),
        ' &sdot; ',
        jQuery('<span>', { className: 'ymd', title: 'Pushed at', text: repo.pushed_at }),
        jQuery('<p>', { text: repo.description })
    );
}

function handleReposJSON(result) {
    jQuery('#projects ul').append(
        jQuery.map(result.repos, createRepoItem)
    );

    jQuery('#projects').show();
}

jQuery(function() {
    jQuery.getJSON('/repos.json', {}, handleReposJSON);
});
