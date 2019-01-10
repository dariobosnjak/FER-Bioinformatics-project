module.exports = function(wallaby) {
  var wallabify = require("wallabify");
  var wallabyPostprocessor = wallabify({
    entryPatterns: ["test/**/*.js"]
  });

  return {
    files: [{ pattern: "src/**/*.ts", load: false }],
    tests: [{ pattern: "test/**/*.test.js", load: false }],
    testFramework: "mocha",
    postprocessor: wallabyPostprocessor,
    setup: function() {
      window.__moduleBundler.loadTests();
    }
  };
};
