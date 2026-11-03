const grid = document.querySelector('#book-grid');
const message = document.querySelector('#message');
const searchInput = document.querySelector('#search-input');
const resultCount = document.querySelector('#result-count');
let books = [];
let activeFilter = 'all';

async function loadBooks() {
  try {
    const response = await fetch('/api/books');
    if (!response.ok) throw new Error('Could not load the catalog.');
    books = await response.json();
    renderBooks();
  } catch (error) {
    message.textContent = `${error.message} Please refresh to try again.`;
  }
}

function renderBooks() {
  const query = searchInput.value.trim().toLowerCase();
  const sortBy = document.querySelector('#sort-select').value;
  const visible = books.filter(book => {
    const matchesText = [book.title, book.author, book.category, book.description]
      .some(value => value.toLowerCase().includes(query));
    const matchesFilter = activeFilter === 'all'
      || (activeFilter === 'available' && book.available)
      || (activeFilter === 'digital' && book.digital);
    return matchesText && matchesFilter;
  }).sort((a, b) => a[sortBy].localeCompare(b[sortBy]));

  resultCount.textContent = `${visible.length} ${visible.length === 1 ? 'book' : 'books'}`;
  grid.innerHTML = visible.length ? visible.map((book, index) => `
    <article class="book-card">
      <div class="book-visual palette-${(book.id - 1) % 5}">
        <span class="visual-category">${escapeHtml(book.category)}</span>
        <span class="visual-title">${escapeHtml(book.title)}</span>
        <span class="visual-author">${escapeHtml(book.author)}</span>
        ${book.digital ? '<span class="digital-stamp">E-BOOK</span>' : ''}
      </div>
      <div class="book-info"><div class="book-title-row"><div><h3>${escapeHtml(book.title)}</h3><p class="author">${escapeHtml(book.author)}</p></div><span class="availability ${book.available ? 'in-stock' : 'checked-out'}" title="${book.available ? 'Available' : 'Borrowed'}"></span></div>
        <p class="description">${escapeHtml(book.description)}</p>
        <div class="book-actions">${book.available
          ? `<button class="primary-button" data-action="borrow" data-id="${book.id}">Borrow book</button>`
          : `<button class="secondary-button" data-action="return" data-id="${book.id}">Return book</button>`}
          ${book.digital ? `<button class="read-button" data-action="read" data-id="${book.id}">Read online <span>↗</span></button>` : '<span class="print-label">PRINT EDITION</span>'}
        </div></div>
    </article>`).join('') : '<div class="empty-state"><span>⌕</span><h3>No books found</h3><p>Try a different search or filter.</p></div>';
}

function escapeHtml(value) {
  return String(value).replace(/[&<>"']/g, character => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
  })[character]);
}

grid.addEventListener('click', async event => {
  const button = event.target.closest('button[data-action]');
  if (!button) return;
  const { action, id } = button.dataset;
  message.textContent = '';
  try {
    if (action === 'read') {
      const response = await fetch(`/api/books/${id}/digital`);
      const result = await response.json();
      if (!response.ok) throw new Error(result.message || 'Digital content could not be opened.');
      document.querySelector('#reader-title').textContent = books.find(book => book.id === Number(id)).title;
      document.querySelector('#reader-content').textContent = result.content;
      document.querySelector('#reader-dialog').showModal();
      return;
    }
    const response = await fetch(`/api/books/${id}/${action}`, { method: 'POST' });
    const updatedBook = await response.json();
    if (!response.ok) throw new Error(updatedBook.message || `Could not ${action} this book.`);
    books = books.map(book => book.id === updatedBook.id ? updatedBook : book);
    renderBooks();
    message.textContent = action === 'borrow' ? 'Book borrowed — happy reading!' : 'Book returned. Thanks for sharing!';
  } catch (error) {
    message.textContent = error.message;
  }
});

document.querySelectorAll('.filter').forEach(button => button.addEventListener('click', () => {
  document.querySelector('.filter.active').classList.remove('active');
  button.classList.add('active');
  activeFilter = button.dataset.filter;
  renderBooks();
}));
searchInput.addEventListener('input', renderBooks);
document.querySelector('#sort-select').addEventListener('change', renderBooks);
document.querySelector('#search-form').addEventListener('submit', event => event.preventDefault());
document.querySelector('#digital-nav').addEventListener('click', () => {
  activeFilter = 'digital';
  document.querySelector('.filter.active').classList.remove('active');
  document.querySelector('[data-filter="digital"]').classList.add('active');
  renderBooks();
});
document.querySelector('#close-reader').addEventListener('click', () => document.querySelector('#reader-dialog').close());
document.querySelector('#reader-dialog').addEventListener('click', event => {
  if (event.target === event.currentTarget) event.currentTarget.close();
});
loadBooks();
