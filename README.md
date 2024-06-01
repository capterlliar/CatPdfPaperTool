### PDF论文信息提取工具
主要功能：提取论文标题、分割或合并PDF、提取文字或图片、打印

#### 安装说明
- 如果您已经安装JDK17及以上Java版本，可直接下载exe文件并运行。
- 否则，请下载exe和jre（由于上传限制无法将它们放在一个压缩包内），并将两者放置于统一目录下，如：  
  <img src="https://github.com/capterlliar/CatPdfPaperTool/blob/master/pics/install.png" width="400px">

------------

#### 0. 导入文件
可以选择菜单中 *导入文件* 或 *导入目录* 选项。如果选择 *导入目录* ，则递归导入该目录下所有PDF文件。

#### 1. 提取论文标题
文件导入后显示在主界面右侧：  
<img src="https://github.com/capterlliar/CatPdfPaperTool/blob/master/pics/fileDisplay.png" width="400px">  

点击 *自动提取所选论文标题* ，论文标题自动替换为检测结果：  
<img src="https://github.com/capterlliar/CatPdfPaperTool/blob/master/pics/rename.png" width="400px">  
如果检测结果不准确，可点击左侧小三角展开候选项选择合适题目，或编辑文本框自行更改。更改完毕点击 *重命名* 按钮更新所选论文标题。

#### 2. 分割或合并PDF
合并PDF：  
<img src="https://github.com/capterlliar/CatPdfPaperTool/blob/master/pics/merge.png" width="400px">  
选择需要合并的文件，并指定导出路径和导出文件名，导出文件为PDF格式，如果导出文件名不以.pdf结尾，软件会自动添加后缀。  

分割PDF：  
<img src="https://github.com/capterlliar/CatPdfPaperTool/blob/master/pics/split.png" width="400px">   
选择需要分割的文件，并指定导出路径。对于每个需要分割的文件，按提示填写所需页数。
导出规则：  
- 对于每个文件，默认将所有导出页面合并在一个PDF中。导出PDF与原文件标题相同，在标题末加数字以区分，如：
  - 原文件名：xxxxx.pdf
  - 导出文件名：xxxxx(1).pdf
- 如果将所选页面需要导出为多个文件，勾选复选框。导出PDF标题为 *原PDF标题+导出页码* ，如：
  - 原文件名：xxxxx.pdf
  - 导出文件名：xxxxx3-5.pdf
- 同时，也可以选择将所有导出页面合并为一个PDF，导出文件标题如：
  - xxxx等3个文件.pdf


#### 3. 提取文字或图片
提取文字：  
<img src="https://github.com/capterlliar/CatPdfPaperTool/blob/master/pics/text.png" width="400px">   
提取图片：  
<img src="https://github.com/capterlliar/CatPdfPaperTool/blob/master/pics/images.png" width="400px">   
提取文字或图片功能使用与分割PDF功能相似。其中，所提取图片默认以所属文件分类，也可以选择全部导出在一个目录下。

#### 4. 打印
<img src="https://github.com/capterlliar/CatPdfPaperTool/blob/master/pics/print.png" width="400px">   
*打印功能尚未连接打印机测试

------------

### 开发环境
Java版本：java17
管理工具： Maven
