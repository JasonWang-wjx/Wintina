import MarkdownIt from 'markdown-it'
import DOMPurify from 'dompurify'

const md = new MarkdownIt({
  html: true,
  linkify: true,
  breaks: true,
})

export const renderMarkdownToHtml = (content: string) => md.render(content || '')

export const sanitizeHtml = (html: string) => DOMPurify.sanitize(html)
