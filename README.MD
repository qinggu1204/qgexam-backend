1.从远程库克隆项目

> git clone git@github.com:qinggu1204/qgexam-backend.git qgexam

然后

> cd qgexam

2.查看远程库信息，为origin说明成功

> git remote -v

3.查看分支(应该只有master分支)

> git branch

4.创建远程origin的dev分支到本地

> git checkout -b dev origin/dev

5.将本地dev与远端dev关联(master默认关联,无需设置)

> git branch --set-upstream-to=origin/dev dev

6.查看关联结果

> git branch -vv

结果为这个则成功

> * dev    e61a165 [origin/dev] 管理远端master分支
>   master e61a165 [origin/master] 管理远端master分支

7.后续在dev分支上开发，push也要把本地的dev分支push到远端的dev分支

> git push origin dev

8.push的时候可能失败，原因是有其他人push了，本地的版本就落后远端的版本，发生冲突

解决先pull拉取远端最新版本，再push

> git pull 

9.如果git pull还有冲突，再说



**push之前先pull**

**push之前先pull**

**push之前先pull**