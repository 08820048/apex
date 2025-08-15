import { marked } from "marked";
import hljs from "highlight.js";

// 导入 highlight.js 主题
import "highlight.js/styles/github-dark.css";

// HTML 转义函数
function escapeHtml(text) {
  const map = {
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#039;",
  };
  return text.replace(/[&<>"']/g, (m) => map[m]);
}

// 初始化 highlight.js
console.log(
  "highlight.js loaded, supported languages:",
  hljs.listLanguages().length
);

// 添加复制代码功能到全局
window.copyToClipboard = function (button) {
  const code = button.getAttribute("data-code");
  navigator.clipboard
    .writeText(code)
    .then(() => {
      const originalText = button.innerHTML;
      button.innerHTML = `
      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
      </svg>
      已复制
    `;
      setTimeout(() => {
        button.innerHTML = originalText;
      }, 2000);
    })
    .catch((err) => {
      console.error("复制失败:", err);
    });
};

// 配置 marked 使用扩展
marked.use({
  renderer: {
    heading(token) {
      const text = this.parser.parseInline(token.tokens);
      const escapedText = text
        .toLowerCase()
        .replace(/[^\w\u4e00-\u9fa5]+/g, "-");
      return `
        <h${token.depth} id="${escapedText}" class="group relative">
          <a href="#${escapedText}" class="header-anchor absolute -left-6 top-0 opacity-0 group-hover:opacity-100 transition-opacity duration-200 text-gray-400 hover:text-blue-500 no-underline" aria-hidden="true">#</a>
          ${text}
        </h${token.depth}>
      `;
    },

    code(token) {
      const code = token.text;
      const language = token.lang || "";

      console.log("Code block:", { language, codeLength: code.length });

      let highlightedCode;
      let detectedLanguage = language;

      try {
        if (language && hljs.getLanguage(language)) {
          // 指定语言高亮
          const result = hljs.highlight(code, { language });
          highlightedCode = result.value;
          console.log("Highlighted with specified language:", language);
        } else {
          // 自动检测语言
          const result = hljs.highlightAuto(code);
          highlightedCode = result.value;
          detectedLanguage = result.language || "text";
          console.log("Auto-detected language:", detectedLanguage);
        }
      } catch (e) {
        console.error("highlight.js error:", e);
        highlightedCode = escapeHtml(code);
        detectedLanguage = "text";
      }

      const escapedCode = escapeHtml(code);

      return `
        <div class="code-block-wrapper">
          <div class="code-block-header">
            <span class="code-language">${
              language || detectedLanguage || "text"
            }</span>
            <button class="copy-button" onclick="copyToClipboard(this)" data-code="${escapedCode}">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
              </svg>
              复制
            </button>
          </div>
          <pre><code class="hljs ${detectedLanguage}">${highlightedCode}</code></pre>
        </div>
      `;
    },

    table(token) {
      const header = this.parser.parse(token.header);
      const body = this.parser.parse(token.rows);
      return `
        <div class="table-wrapper">
          <table class="markdown-table">
            <thead>${header}</thead>
            <tbody>${body}</tbody>
          </table>
        </div>
      `;
    },

    link(token) {
      const href = token.href;
      const title = token.title;
      const text = this.parser.parseInline(token.tokens);
      const isExternal =
        href.startsWith("http") && !href.includes(window.location.hostname);
      const target = isExternal
        ? ' target="_blank" rel="noopener noreferrer"'
        : "";
      const titleAttr = title ? ` title="${title}"` : "";

      return `<a href="${href}"${titleAttr}${target} class="markdown-link">${text}</a>`;
    },

    image(token) {
      const href = token.href;
      const title = token.title;
      const text = token.text;
      const titleAttr = title ? ` title="${title}"` : "";
      return `
        <div class="image-wrapper">
          <img src="${href}" alt="${text}"${titleAttr} class="markdown-image" loading="lazy" />
          ${title ? `<p class="image-caption">${title}</p>` : ""}
        </div>
      `;
    },

    blockquote(token) {
      const quote = this.parser.parse(token.tokens);
      return `<blockquote class="markdown-blockquote">${quote}</blockquote>`;
    },
  },

  gfm: true,
  breaks: true,
});

// 导出渲染函数
export function renderMarkdown(content) {
  if (!content) return "";
  return marked(content);
}

export default {
  render: renderMarkdown,
};
