const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const HtmlWebPackPlugin = require("html-webpack-plugin");
const ModuleFederationPlugin = require("webpack/lib/container/ModuleFederationPlugin");
const path = require("path");
const Dotenv = require("dotenv-webpack");

const deps = require("./package.json").dependencies;

const printCompilationMessage = require("./compilation.config.js");

module.exports = (_, argv) => ({
  output: {
    publicPath: "auto",
  },

  resolve: {
    extensions: [".tsx", ".ts", ".jsx", ".js", ".json"],
  },

  devServer: {
    port: 9001,
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers": "Authorization, Content-Type",
    },
    historyApiFallback: true,
    watchFiles: [path.resolve(__dirname, "src")],
    onListening: function (devServer) {
      const port = devServer.server.address().port;

      printCompilationMessage("compiling", port);

      devServer.compiler.hooks.done.tap("OutputMessagePlugin", (stats) => {
        setImmediate(() => {
          if (stats.hasErrors()) {
            printCompilationMessage("failure", port);
          } else {
            printCompilationMessage("success", port);
          }
        });
      });
    },
  },

  module: {
    rules: [
      {
        test: /\.m?js/,
        type: "javascript/auto",
        resolve: {
          fullySpecified: false,
        },
      },
      {
        test: /\.(css|s[ac]ss)$/i,
        use: [MiniCssExtractPlugin.loader, "css-loader", "postcss-loader"],
      },
      {
        test: /\.(ts|tsx|js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
        },
      },
    ],
  },

  plugins: [
    new MiniCssExtractPlugin({
      filename: "styles.css",
    }),

    new ModuleFederationPlugin({
      name: "auth",
      filename: "remoteEntry.js",
      exposes: {
        "./AuthLogic": "./src/AuthLogic.jsx",
        "./styles": "./src/stylesEntry.js",
      },
      shared: {
        ...deps,
        react: {
          singleton: true,
          eager: true,
          requiredVersion: deps.react,
        },
        "react-dom": {
          singleton: true,
          eager: true,
          requiredVersion: deps["react-dom"],
        },
        axios: {
          singleton: true,
          eager: true,
          requiredVersion: deps.axios,
        },
      },
    }),
    new HtmlWebPackPlugin({
      template: "./src/index.html",
    }),
    new Dotenv(),
  ],
});
