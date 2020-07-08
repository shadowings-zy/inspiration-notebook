const path = require('path')
const CompressionWebpackPlugin = require("compression-webpack-plugin");
const config = require("./config.json")
const productionGzipExtensions = /\.(js|css|json|txt|html|ico|svg)(\?.*)?$/i;

const resolve = dir => {
  return path.join(__dirname, dir)
}

const BASE_URL = process.env.NODE_ENV === 'production' ? './' : '/'

module.exports = {
  publicPath: BASE_URL,
  outputDir: 'dist',
  assetsDir: 'static/',
  lintOnSave: true,
  productionSourceMap: false,
  devServer: {
    proxy: {
      '/api': {
        target: config.devServerUrl,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  configureWebpack: config => {
    const plugins = [];

    // 生成 gzip 压缩文件
    plugins.push(
      new CompressionWebpackPlugin({
        filename: "[path].gz[query]",
        algorithm: "gzip",
        test: productionGzipExtensions,
        threshold: 10240,
        minRatio: 0.8
      })
    );
    config.plugins = [...config.plugins, ...plugins];

    // 开发环境配置source-map
    if (process.env.NODE_ENV !== 'production') {
      config.devtool = 'source-map'
    }
  }
}
