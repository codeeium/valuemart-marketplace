function helloWorld() {
  return 'Hello, World!';
}

// Export for Node.js module system
if (typeof module !== 'undefined' && module.exports) {
  module.exports = { helloWorld };
}

// Run directly with node hello.js
if (require.main === module) {
  console.log(helloWorld());
}
