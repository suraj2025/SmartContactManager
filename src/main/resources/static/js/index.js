document.addEventListener("DOMContentLoaded", function () {
    const msg = document.getElementById("toastContent")?.value;
    const type = document.getElementById("toastType")?.value;

    if (msg) {
      const toast = document.createElement("div");
      toast.className = `toast-message bg-${type}`;
      toast.textContent = msg;

      // Progress Bar Element
      const progress = document.createElement("div");
      progress.className = "toast-progress";
      toast.appendChild(progress);

      document.body.appendChild(toast);

      // Show animation
      setTimeout(() => {
        toast.style.bottom = "30px";
        toast.style.opacity = "1";
      }, 100);

      // Hide after 3 seconds
      setTimeout(() => {
        toast.style.bottom = "-100px";
        toast.style.opacity = "0";
        setTimeout(() => toast.remove(), 500);
      }, 5000);
    }
  });
lottie.loadAnimation({
	container: document.getElementById('lottie-container'),
	renderer: 'svg',
	loop: true,
	autoplay: true,
	path: '../lottie/register.json' // Make sure this path is correct in your static folder
});

const toggleBtn = document.getElementById("sidebarToggle");
const sidebar = document.getElementById("sidebar");
const content = document.getElementById("contentArea");

toggleBtn.addEventListener("click", () => {
	sidebar.classList.toggle("collapsed");
	content.classList.toggle("expanded");
});


setTimeout(() => {
	const alertBox = document.getElementById('alert-box');
	if (alertBox) {
		alertBox.style.display = 'none';
	}
}, 3000);



		
const viewModal = document.getElementById('viewModal');
	const mainContent = document.getElementById('mainContent');

	viewModal.addEventListener('show.bs.modal', function (event) {
		const button = event.relatedTarget;

		// Populate modal content
		document.getElementById('modalName').textContent = button.getAttribute('data-name');
		document.getElementById('modalEmail').textContent = button.getAttribute('data-email');
		document.getElementById('modalPhone').textContent = button.getAttribute('data-phone');
		document.getElementById('modalWork').textContent = button.getAttribute('data-work');
		document.getElementById('modalDesc').textContent = button.getAttribute('data-desc');
		document.getElementById('modalImage').src = button.getAttribute('data-img');

		// Apply inline blur
		mainContent.style.filter = "blur(5px)";
		mainContent.style.transition = "filter 0.3s ease-in-out";
		mainContent.style.pointerEvents = "none";
		
	});

	viewModal.addEventListener('hidden.bs.modal', function () {
		// Remove blur
		mainContent.style.filter = "";
		mainContent.style.pointerEvents = "";
	});
	
	document.addEventListener("DOMContentLoaded", function () {
	    const searchInput = document.getElementById("contactSearch");
	    const resultBox = document.getElementById("searchResults");

	    let allContacts = [];

	    // Fetch all contacts on page load
	    fetch("/allContact")
	        .then(res => res.json())
	        .then(data => {
	            allContacts = data;
	        })
	        .catch(err => console.error("Failed to fetch contacts:", err));

	    searchInput.addEventListener("keyup", function () {
	        const query = this.value.trim().toLowerCase();
	        resultBox.innerHTML = "";

	        if (query === "") {
	            resultBox.style.display = "none";
	            return;
	        }

	        const matched = allContacts.filter(c =>
	            `${c.name} ${c.secondName}`.toLowerCase().includes(query)
	        );

	        if (matched.length === 0) {
	            resultBox.innerHTML = `<div class="list-group-item text-muted">No results found</div>`;
	        } else {
	            matched.forEach(contact => {
	                const item = document.createElement("a");
	                item.href = "#";
	                item.className = "list-group-item list-group-item-action";
	                item.textContent = `${contact.name} ${contact.secondName}`;
	                item.onclick = (e) => {
	                    e.preventDefault();
	                    showContactDetails(contact); // define this
	                    resultBox.innerHTML = "";
	                    searchInput.value = "";
	                    resultBox.style.display = "none";
	                };
	                resultBox.appendChild(item);
	            });
	        }

	        resultBox.style.display = "block";
	    });

	    function showContactDetails(contact) {
	        // Show modal logic using contact object
	        document.getElementById('modalName').textContent = `${contact.name} ${contact.secondName}`;
	        document.getElementById('modalEmail').textContent = contact.email;
	        document.getElementById('modalPhone').textContent = contact.phone;
	        document.getElementById('modalWork').textContent = contact.work;
	        document.getElementById('modalDesc').textContent = contact.desc;
	        document.getElementById('modalImage').src = `/image/${contact.image}`;

	        new bootstrap.Modal(document.getElementById('viewModal')).show();
	    }
	});
	
	