# SimpleMarkers

**SimpleMarkers** is a lightweight Spigot plugin for creating floating text markers using invisible armor stands.

Perfect for servers that need clean, non-intrusive in-world labels for events, zones, instructions, etc.

---

## 📸 Screenshots
Soon!

---

## 🚀 Features

- 📍 Create floating multi-line markers using `/sm create`
- ❌ Easily remove nearby markers with `/sm delete`
- 🌍 Localization support (default `messages.yml` and optional `messages_<locale>.yml` files in `messages/` folder — ready to paste)
- 🔒 Optional permissions system via config
---

## 🌐 Included Translations

- 🇬🇧 English (`en_US.yml`)
- 🇩🇪 German (`de_DE.yml`)
- 🇫🇷 French (`fr_FR.yml`)
- 🇪🇸 Spanish (`es_ES.yml`)
- 🇷🇺 Russian (`ru_RU.yml`)
- 🇵🇱 Polish (`pl_PL.yml`)

---

## 📦 Installation
**SimpleMarkers** is compatible with Spigot, Paper, and other forks of Bukkit.
Tested on Paper 1.21 , should work on 1.13 
Requires: Java 8+


1. Download the plugin `.jar` from Releases and place it in your server's `plugins/` folder.
2. Start the server once to generate the config and message files.
3. (Optional) Configure permissions or language support.

---

## 🧠 Commands

| Command              | Description                                 | Permission (if enabled)           |
|----------------------|---------------------------------------------|-----------------------------------|
| `/sm create <text>`  | Creates a floating marker at your location. | `simplemarkers.create`            |
| `/sm delete`         | Deletes markers within 1 block.             | `simplemarkers.delete`            |
| `/sm`                | Displays usage info.                        | `simplemarkers.use`               |

**Notes:**
- Use `\n` for new lines.
- Use `&` for color codes (e.g. `&cRed`, `&lBold`, etc). [Full list of colors, not affiliated with this plugin](https://www.spigotmc.org/wiki/minecraft-color-codes/).

---

## ⚙ Configuration

**`config.yml`**
```yaml
permissions:
  use: true
  create: true
  delete: true
```