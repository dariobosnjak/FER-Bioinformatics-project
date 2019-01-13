var gulp = require("gulp"),
  tsify = require("tsify"),
  tsc = require("gulp-typescript"),
  tslint = require("gulp-tslint"),
  istanbul = require("gulp-istanbul"),
  mocha = require("gulp-mocha"),
  browserify = require("browserify"),
  source = require("vinyl-source-stream"),
  buffer = require("vinyl-buffer"),
  sourcemaps = require("gulp-sourcemaps"),
  browserSync = require("browser-sync").create(),
  runSequence = require("run-sequence");

gulp.task("lint", function() {
  var config = {
    formatter: "verbose",
    emitError: process.env.CI ? true : false
  };

  return gulp
    .src(["src/**/**.ts", "test/**/**.test.ts"])
    .pipe(tslint(config))
    .pipe(tslint.report());
});

var tsProject = tsc.createProject("tsconfig.json");

gulp.task("build-js", function() {
  return gulp
    .src(["src/**/**.ts", "test/**/**.test.ts"], { base: "." })
    .pipe(tsProject())
    .on("error", function(err) {
      process.exit(1);
    })
    .js.pipe(gulp.dest("."));
});

gulp.task("istanbul:hook", function() {
  return gulp
    .src(["src/**/*.js"])
    .pipe(istanbul())
    .pipe(istanbul.hookRequire());
});

gulp.task("test", ["istanbul:hook"], function() {
  return gulp
    .src("test/**/*.test.js")
    .pipe(mocha({ ui: "bdd" }))
    .pipe(istanbul.writeReports());
});

gulp.task("build", function() {
  var libraryName = "lcsk";
  var mainTsFilePath = "src/main.ts";
  var outputFolder = "dist/";
  var outputFileName = "main" + ".min.js";

  var bundler = browserify({
    debug: true,
    standalone: libraryName
  });

  return bundler
    .add(mainTsFilePath)
    .plugin(tsify, { noImplicitAny: true })
    .bundle()
    .on("error", function(err) {
      console.error(err.toString());
    })
    .pipe(source(outputFileName))
    .pipe(buffer())
    .pipe(sourcemaps.write("."))
    .pipe(gulp.dest(outputFolder));
});

gulp.task("watch-build", function() {
  gulp.watch(["src/**/**.ts", "test/**/**.ts"], ["build"]);
});

gulp.task("watch", function() {
  gulp.watch(["src/**/**.ts", "test/**/**.ts"], ["default"]);
});

gulp.task("default", function(cb) {
  runSequence("lint", "build-js", "test", "build", cb);
});
