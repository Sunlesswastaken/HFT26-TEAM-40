---
name: hackflow-ppt
description: Generate hackathon-ready presentation slide images and PPTX decks using Codex built-in image generation, especially when the user asks for /ppt, PPT, slides, pitch decks, project demos, or replacing Gemini/Yunwu/third-party PPT image generation with gpt-image-2.
metadata:
  short-description: Generate PPT images with built-in image generation
---

# HackFlow PPT

Create a PPT deck by planning slides, generating one 16:9 raster image per slide with Codex built-in `image_gen`, and assembling the images into a PPTX locally.

Use this instead of third-party image APIs. Do not request Gemini, Yunwu, or proxy API keys.

**Plugin scripts directory**: `{baseDir}/scripts`

## Workflow

1. Determine the topic:
   - If the user provides a topic or `$ARGUMENTS`, use it.
   - If they say "about this project" or give no topic, analyze the current workspace: structure, README, package files, and main source files.
2. Pick a style. Default to `gradient-glass` unless the user requests another style. Available styles:
   - `gradient-glass`
   - `apple-vision`
   - `google-quantum`
   - `microsoft-fluent`
   - `vercel-mono`
   - `linear-mesh`
3. Create a slide plan. Default to 8 pages for hackathon/project decks and 5-6 pages for quick demos.
4. Write the plan to `.codex/ppt/plan.json` in the current workspace.
5. Generate prompts from the plan:
   ```bash
   python3 "{baseDir}/scripts/make_prompts.py" \
     --plan "$(pwd)/.codex/ppt/plan.json" \
     --out "$(pwd)/.codex/ppt/prompts.json"
   ```
6. For each prompt in `prompts.json`, call Codex built-in `image_gen` directly. Generate exactly one slide image per prompt.
7. Save each final slide image into `$(pwd)/.codex/ppt/slides/` as `slide-01.png`, `slide-02.png`, etc. If the built-in tool saves elsewhere first, move or copy the selected image into that folder.
8. Assemble PPTX:
   ```bash
   python3 "{baseDir}/scripts/assemble_pptx.py" \
     --images "$(pwd)/.codex/ppt/slides" \
     --out "$(pwd)/$(basename "$(pwd)")-hackflow-ppt.pptx" \
     --report "$(pwd)/.codex/ppt/report.json"
   ```
9. Report the PPTX path, slide image folder, and any skipped pages.

## Plan Format

```json
{
  "style_id": "gradient-glass",
  "slides": [
    {
      "page_index": 1,
      "page_type": "cover",
      "content": "Title: ...\nSubtitle: ..."
    },
    {
      "page_index": 2,
      "page_type": "content",
      "content": "..."
    }
  ]
}
```

Allowed `page_type`: `cover`, `content`, `data`.

## Slide Writing Rules

- Default language is Chinese unless the user asks otherwise.
- Keep slide text short. Prefer phrases over paragraphs.
- Page 2 should open with a relatable pain point for hackathon/project decks.
- Data pages should use big numbers, percentages, or comparisons.
- Each prompt must explicitly ask for a polished 16:9 presentation slide, not a poster.
- Avoid tiny body text, crowded layouts, watermarks, fake UI chrome, and unreadable decorative text.

## Built-In Image Generation Prompt Guardrail

Before each generated slide prompt, add:

```text
Create one complete 16:9 presentation slide image. The slide must be readable at presentation size, with clean typography, no watermark, no mockup border, and no extra text beyond the requested slide content.
```
