// ADICIONAR
function confirmarAdicao(formId, mensagemAdicao) {
	const form = document.getElementById(formId);
	if (form) {
    	form.addEventListener('submit', function(event) {
      		event.preventDefault();
	  
		    Swal.fire({
		        title: 'Confirmação',
		        text: mensagemAdicao,
		        icon: 'question',
		        showCancelButton: true,
		        confirmButtonColor: '#3085d6',
		        cancelButtonColor: '#d33',
		        confirmButtonText: 'Confirmar',
		        cancelButtonText: 'Cancelar'
			}).then((result) => {
		        if (result.isConfirmed) {
		          form.submit();
		        }
			});
    	});
	}
}
// Aplicar a função aos formulários desejados
confirmarAdicao('form-funcionario-adicao', 'Deseja adicionar este funcionario?');
confirmarAdicao('form-cliente-adicao', 'Deseja adicionar este cliente?');
confirmarAdicao('form-produto-adicao', 'Deseja adicionar este produto?');
confirmarAdicao('form-entrada-adicao', 'Deseja adicionar esta entrada?');
confirmarAdicao('form-saida-adicao', 'Deseja adicionar esta saida?');

// ATUALIZAR
function confirmarAtualizacao(formId, mensagemAtualizacao) {
	const form = document.getElementById(formId);
	if (form) {
    	form.addEventListener('submit', function(event) {
      		event.preventDefault();
	  
		    Swal.fire({
		        title: 'Confirmação',
		        text: mensagemAtualizacao,
		        icon: 'question',
		        showCancelButton: true,
		        confirmButtonColor: '#3085d6',
		        cancelButtonColor: '#d33',
		        confirmButtonText: 'Confirmar',
		        cancelButtonText: 'Cancelar'
			}).then((result) => {
		        if (result.isConfirmed) {
		          form.submit();
		        }
			});
    	});
	}
}
// Aplicar a função aos formulários desejados
confirmarAtualizacao('form-funcionario-atualizacao', 'Deseja atualizar este funcionario?');
confirmarAtualizacao('form-cliente-atualizacao', 'Deseja atualizar este cliente?');
confirmarAtualizacao('form-produto-atualizacao', 'Deseja atualizar este produto?');
confirmarAtualizacao('form-entrada-atualizacao', 'Deseja atualizar esta entrada?');
confirmarAtualizacao('form-saida-atualizacao', 'Deseja atualizar esta saida?');

// VAI PARA A PAGINA DE EDIÇÃO
function confirmarEdicao(element) {
    event.preventDefault();
    const url = element.getAttribute('data-url');

    Swal.fire({
        title: 'Deseja realmente editar?',
        text: 'Você será redirecionado para a tela de edição.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Editar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = url;
        }
    });
}

// EXCLUIR
function confirmarExclusao(element) {
    event.preventDefault(); // Impede navegação imediata
    const url = element.getAttribute('data-url');

    Swal.fire({
        title: 'Deseja realmente excluir?',
        text: 'Essa ação não poderá ser desfeita!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Excluir',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = url;
        }
    });
}
