document.addEventListener('DOMContentLoaded', () =>
{
    document.getElementById("user-form").addEventListener('submit', async function(e) {
        e.preventDefault();
        const data = {
            name: document.getElementById('user-name').value,
            email: document.getElementById('user-email').value,
            senha: document.getElementById('user-password').value
        };
        try {
            const response = await fetch('/api/v1/registrar/usuario',
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
            const dataResponse = await response.json();
            if(response.ok)
            {
                alert("Usuario inserido com sucesso!");
            }
            else
            {
                alert(JSON.stringify(dataResponse));
            }
        }
        catch (error)
        {
            alert('Error: ' + error.message);
        }
    }); //END POST
});

document.getElementById("get-user-form").addEventListener("submit", async function (a)
{
    a.preventDefault();
    try {
        let id = document.getElementById("get-user-id").value;
        let endpoint = `/api/v1/getUser/${id}`;
        const response = await fetch(endpoint,
            {
                method : 'GET',
                headers : {'Content-Type': 'application/json'}
            })
        const data = await response.json();
        alert(JSON.stringify(data));
        //
    } catch(error)
    {
        alert('Error: ' + error.message);
    }

})// END GET BY ID

document.getElementById("delete-user-form").addEventListener("submit", async function (d)
{
    d.preventDefault();
    try {
        let id = document.getElementById("delete-user-id").value;
        let endpoint = `/api/v1/delete/usuario/${id}`;
        const response = await fetch(endpoint,
            {
                    method : "DELETE",
                    headers : {'Content-Type': 'application/json'}
                })
            const data = await response.json();
            alert(JSON.stringify(data));
    } catch (error){
        alert("Erro ao deletar!");
    }
}); //END DELETE

document.getElementById("user-form_update").addEventListener("submit", async function (d)
{
    d.preventDefault();
    try {
        let data = {
            nome: document.getElementById('user-name-update').value,
            email: document.getElementById('user-email-update').value,
            senha: document.getElementById('user-password-update').value
        }
        let id = document.getElementById("update-user-getid").value;
        const response = await fetch(`api/v1/update/user/${id}`, {
            method: "PUT",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        })
        const dataResponse = await response.json();
        if (response.ok) {
            alert("Usuario atualizado com sucesso: " + JSON.stringify(dataResponse));
        } else
        {
            alert(JSON.stringify(dataResponse));
        }
    } catch (e) {
        alert("Erro ao atualizar!")
    }
}); //END UPDATE
