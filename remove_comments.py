import os
import re

# Đường dẫn thư mục chứa mã nguồn
ROOT = os.path.join(os.path.dirname(__file__), 'src')

# Regex cho comment kiểu // và /* ... */
LINE_COMMENT = re.compile(r'//.*')
BLOCK_COMMENT = re.compile(r'/\*.*?\*/', re.DOTALL)

# Duyệt toàn bộ file .java
for dirpath, _, filenames in os.walk(ROOT):
    for filename in filenames:
        if filename.endswith('.java'):
            path = os.path.join(dirpath, filename)
            with open(path, 'r', encoding='utf-8') as f:
                code = f.read()
            # Xóa comment dạng /* ... */
            code = BLOCK_COMMENT.sub('', code)
            # Xóa comment dạng // ...
            code = '\n'.join([LINE_COMMENT.sub('', line) for line in code.splitlines()])
            # Xóa dòng trống dư thừa
            code = '\n'.join([line for line in code.splitlines() if line.strip() != ''])
            with open(path, 'w', encoding='utf-8') as f:
                f.write(code)
print('file Java!')
