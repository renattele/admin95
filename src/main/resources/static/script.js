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