
// Minimal frontend JS to call simple APIs
async function postJSON(url, body) {
  const res = await fetch(url, {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(body)
  });
  return res.json();
}

document.addEventListener('DOMContentLoaded', ()=>{
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', async (e)=>{
      e.preventDefault();
      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;
      try {
        const r = await postJSON('/api/auth', {email, password});
        if (r.status==='OK') {
          if (r.role==='ADMIN') window.location.href = '/admin.html';
          else window.location.href = '/user.html';
        } else {
          document.getElementById('message').innerText = r.message || 'Login failed';
        }
      } catch (err) {
        document.getElementById('message').innerText = 'Error: '+err.message;
      }
    });
  }

  const pingBtn = document.getElementById('pingBtn');
  if (pingBtn) {
    pingBtn.addEventListener('click', async ()=>{
      try {
        const res = await fetch('/api/admin/ping');
        const j = await res.json();
        document.getElementById('pingResult').innerText = j.message;
      } catch (e) { document.getElementById('pingResult').innerText = e.message; }
    });
  }

  const createTx = document.getElementById('createTx');
  if (createTx) {
    createTx.addEventListener('click', async ()=>{
      const amount = parseFloat(document.getElementById('amount').value||0);
      if (!amount) { document.getElementById('txResult').innerText = 'Enter amount'; return; }
      try {
        const r = await postJSON('/api/transactions', {userId:1, amount});
        document.getElementById('txResult').innerText = 'Created txn id='+r.id;
      } catch (e) { document.getElementById('txResult').innerText = e.message; }
    });
  }

  // load pending transactions on user page
  const list = document.getElementById('list');
  if (list) {
    fetch('/api/transactions').then(r=>r.json()).then(data=>{
      if (Array.isArray(data)) {
        list.innerHTML = data.map(t=> '<div>Tx '+t.id+': $'+t.amount+' - '+t.status+'</div>').join('');
      } else list.innerText = JSON.stringify(data);
    }).catch(e=>list.innerText=e.message);
  }
});
