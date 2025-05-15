function debounce(timeoutMs, callee) {
    return (...args) => {
        let previousCall = this.lastCall;
        this.lastCall = Date.now();
        if (previousCall && this.lastCall - previousCall <= timeoutMs) {
            clearTimeout(this.lastCallTimer);
        }
        this.lastCallTimer = setTimeout(() => callee(...args), timeoutMs);
    }
}

function reload() {
    window.location.reload();
}


const confirmDeletion = (object) => {
    return window.confirm(`Are you sure you want to delete ${object}?`);
}

const formDataToJson = (form) => {
    const object = {};
    form.forEach((value, key) => object[key] = value);
    return object;
};

const fetchHandler = errorMessage => {
    return (response) => {
        if (response.ok) reload();
        else alert(errorMessage);
    };
}

const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

/**
 * Enhanced fetch that automatically adds CSRF token to requests
 * @param {string} url - The URL to fetch
 * @param {object} options - Fetch options
 * @returns {Promise} - Fetch promise
 */
const serverFetch = (url, options = {}) => {
    // Create a new options object with defaults
    const fetchOptions = {
        ...options,
        headers: {
            ...options.headers,
            'X-XSRF-TOKEN': csrfToken
        }
    };

    // Return the native fetch with enhanced options
    return fetch(url, fetchOptions);
};