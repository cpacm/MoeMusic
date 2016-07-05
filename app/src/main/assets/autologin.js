function getEle(id) {
	return document.getElementById(id);
}

function getElementsByClassName(n) {
	var classElements = document.getElementsByClassName(n);
	return classElements[0];
}

function dispatch(c, b) {
	c.click();
	var a = document.createEvent("Event");
	a.initEvent(b, true, true);
	c.dispatchEvent(a);
}

function isVisible (e) {
  return  e.clientWidth !== 0 &&
    e.clientHeight !== 0 &&
    e.style.opacity !== 0 &&
    e.style.visibility !== 'hidden';
}

function autologin(acc,pwd) {
	var account = getEle('login_account'),  passwd = getEle('login_password'),  gobt = getElementsByClassName('btn-submit');
	if(account!=null) {
		account.value = acc;
		passwd.value = pwd;
		dispatch(gobt,"click");
	}else{
		dispatch(gobt,"click");
	}
}


function getup() {
	var u = getElementsByClassName('msg');
	if(isVisible(u)){
		oauth.getResult(u.textContent);
	}
}


function adduplistener() {
	var g = getElementsByClassName('btn-submit');
	if (typeof g.addEventListener != "undefined") {
		g.addEventListener("click",getup,true);} 
	else {
		g.attachEvent("click",getup);
	} 
}



