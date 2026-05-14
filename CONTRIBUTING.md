# Contributing to LibrePDF

Thanks for your interest in LibrePDF! Contributions are welcome — but with a clear philosophy.

## Philosophy

LibrePDF has reached its 8 core features (Merge, Split, Flatten, PDF Info, Clean Metadata, PDF to JPEG, Protect, Unlock). **The feature scope is closed.** From here on, the focus is polish: fixing bugs, improving UX, adding translations, and keeping the codebase clean.

Better 8 polished features than 15 half-broken ones.

## What's welcome

- **Bug fixes** — open issues are good candidates.
- **UX / UI improvements** — CSS, layout, accessibility, keyboard shortcuts.
- **New translations** — beyond the current `en`, `it`, `fr`, `de`, `es`.
- **Performance & memory** improvements.
- **Linux testing & fixes** — see the platform support table in the README.
- **Tests** — more coverage is always welcome.

## What's out of scope

- **New PDF operations** (OCR, watermark, redact, compress, rotate, etc.). Please don't open PRs for these — they won't be merged.
- **Anything that makes a network call.** No cloud sync, no telemetry, no "AI assistants", no update checks, no analytics, no remote anything. PRs that introduce HTTP calls will be nuked on sight.
- **Changes to the threat model.** LibrePDF is and stays a single-user offline app.

## Before opening a PR

- For non-trivial changes, **open an issue first** to discuss.
- Run `mvn test` — all tests must pass.
- Follow the existing code style: small, focused changes, no over-engineering, no obvious comments.
- Use [Conventional Commits](https://www.conventionalcommits.org/) in commit messages: `fix:`, `feat:`, `docs:`, `refactor:`, `test:`, `ci:`.

## Adding a translation

1. Copy `src/main/resources/i18n/messages_en.properties` to `messages_<lang>.properties` (e.g. `messages_pt.properties` for Portuguese).
2. Translate the values, keep the keys unchanged.
3. Make sure the file is saved as UTF-8.
4. Open a PR.

## Reporting bugs

Open an [issue](../../issues/new/choose) and fill in the bug report template. The more reproducible the report, the faster it gets fixed.

For anything that isn't a bug, please email **info@leonardomontemurro.it** instead of opening a generic issue.
