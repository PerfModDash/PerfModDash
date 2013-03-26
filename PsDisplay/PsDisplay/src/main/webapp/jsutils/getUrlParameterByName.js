/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function getUrlParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}
