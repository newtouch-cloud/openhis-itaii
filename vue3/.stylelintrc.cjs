/*
 * @Author: yangbo@bjgoodwill.com
 * @Date: 2024-10-14 09:52:54
 * @Description:
 */
module.exports = {
    plugins: ['stylelint-scss'],
    extends: [
      'stylelint-config-standard',
      'stylelint-config-standard-scss',
      'stylelint-config-recommended',
      'stylelint-config-recommended-vue/scss',
      'stylelint-config-rational-order',
    ],
    overrides: [
      {
        files: ['*.scss', '**/*.scss'],
        customSyntax: 'postcss-scss',
      },
      {
        files: ['*.html', '**/*.html', '*.vue', '**/*.vue'],
        customSyntax: 'postcss-html',
      },
    ],
    rules: {
      'no-descending-specificity': null,
      'font-family-no-missing-generic-family-keyword': null,
      'selector-type-no-unknown': null,
      'at-rule-no-unknown': null,
      'no-duplicate-selectors': null, // 关闭强制选择器类名的格式
      'no-empty-source': null, // 关闭禁止空源码
      'selector-class-pattern': null,
      'number-leading-zero': null,
      'scss/dollar-variable-pattern': /[a-z][a-zA-Z]+/,
      'color-function-notation': 'legacy',
      'selector-pseudo-class-no-unknown': [
        // 不允许未知的选择器
        true,
        {
          ignorePseudoClasses: ['deep'], // 忽略属性，修改
        },
      ],
    },
  };
  