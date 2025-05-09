import { confirmarAdicao } from './fragments/confirmacoes.js';
import { toggleTheme, aplicarTemaSalvo } from './fragments/tema.js';

window.addEventListener('DOMContentLoaded', () => {
    confirmarAdicao();
	toggleTheme();
    aplicarTemaSalvo();
});
