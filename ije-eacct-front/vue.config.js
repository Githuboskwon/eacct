const path = require('path');
const ansiRegex = require('ansi-regex');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  productionSourceMap: process.env.NODE_ENV !== 'production', //운영 배포시 디버깅 close
  runtimeCompiler: true,
  transpileDependencies: [ansiRegex],
  filenameHashing: true,
  configureWebpack: config => {
    config.output.filename = "[name].[hash].js";
    config.output.chunkFilename = "[name].[hash].js";
  },
  configureWebpack: {
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          use: 'ts-loader',
          exclude: /node_modules/,
        },
        {
          test: /\.(sa|sc|c)ss$/,
          use: ["postcss-loader", "sass-loader"]
        }
      ]
    },
    plugins: [
      new HtmlWebpackPlugin({
        template: 'public/index.html'
      })
    ],
    resolve: {
      extensions: [
        ".d.ts"
      ]
    }
  },
  outputDir: path.resolve(__dirname, 'dist'),
  chainWebpack: config => {
    config.resolve.alias
    .set('@', path.resolve(__dirname, 'src/'));
  },
  publicPath: '/',
  devServer: {
    port: process.env.VUE_APP_PORT
  }
};
