
import sys
import re

file_path = 'D:/RSPS/KronosSSL/server/data/cache/toml/1_patches/interface/119.toml'

with open(file_path, 'r') as f:
    lines = f.readlines()

output = []
skip = False
for line in lines:
    # Check for [204] or [205] as exact lines
    if line.strip() == '[204]' or line.strip() == '[205]':
        skip = True
        continue
    # If we are skipping, and we hit another [ID] line, stop skipping
    if skip and re.match(r'^\[\d+\]', line.strip()):
        skip = False
    
    if not skip:
        output.append(line)

# Clean up trailing whitespace/newlines
content = "".join(output).strip() + "\n\n"
output = [content]

# Add components 204 to 503
for i in range(204, 504):
    y = (i - 4) * 20
    output.append(f'[{i}]\n')
    output.append(f'x = 0\n')
    output.append(f'y = {y}\n')
    output.append(f'width = 0\n')
    output.append(f'height = 20\n')
    output.append(f'content = 0\n')
    output.append(f'parent = 3\n')
    output.append(f'\n')
    output.append(f'[{i}.cs2]\n')
    output.append(f'align_x = "Center"\n')
    output.append(f'align_y = "Min"\n')
    output.append(f'align_width = "Minus"\n')
    output.append(f'align_height = "Abs"\n')
    output.append(f'actions = []\n')
    output.append(f'\n')
    output.append(f'[{i}.cs2.Text]\n')
    output.append(f'alignment_x = "Center"\n')
    output.append(f'alignment_y = "Min"\n')
    output.append(f'font = 495\n')
    output.append(f'line_height = 0\n')
    output.append(f'text = ""\n')
    output.append(f'shadowed = false\n')
    output.append(f'color = 0\n')
    output.append(f'\n')

# Add scrollbar at 504
output.append('[504]\n')
output.append('x = 44\n')
output.append('y = 84\n')
output.append('width = 16\n')
output.append('height = 215\n')
output.append('content = 0\n')
output.append('parent = 0\n')
output.append('\n')
output.append('[504.cs2]\n')
output.append('align_x = "Max"\n')
output.append('align_y = "Min"\n')
output.append('align_width = "Abs"\n')
output.append('align_height = "Abs"\n')
output.append('actions = []\n')
output.append('\n')
output.append('[504.cs2.Layer]\n')
output.append('scroll_width = 0\n')
output.append('scroll_height = 0\n')
output.append('no_click_through = false\n')
output.append('\n')

# Add close button at 505
output.append('[505]\n')
output.append('x = 45\n')
output.append('y = 54\n')
output.append('width = 26\n')
output.append('height = 23\n')
output.append('content = 0\n')
output.append('parent = 0\n')
output.append('\n')
output.append('[505.cs2]\n')
output.append('align_x = "Max"\n')
output.append('align_y = "Min"\n')
output.append('align_width = "Abs"\n')
output.append('align_height = "Abs"\n')
output.append('actions = ["Close"]\n')
output.append('click_mask = 2\n')
output.append('on_mouse_over = "44(id, 542)"\n')
output.append('on_mouse_leave = "44(id, 541)"\n')
output.append('on_op = "29()"\n')
output.append('\n')
output.append('[505.cs2.Sprite]\n')
output.append('sprite = 541\n')
output.append('texture = 0\n')
output.append('tilling = false\n')
output.append('opacity = 0\n')
output.append('border_kind = 0\n')
output.append('shadow_color = 0\n')
output.append('flipped_vertically = false\n')
output.append('flipped_horizontally = false\n')

with open(file_path, 'w') as f:
    f.writelines(output)
