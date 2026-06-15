module.exports = {
  presets: [
    [
      "@babel/preset-env",
      {
        "corejs": "3",
        // "modules": "commonjs",
        "useBuiltIns": "entry",
        "debug": false,
        targets: {
          esmodules: true
        },
      }
    ]
  ],
  "compact" : true
};