:: 用于 release 当前项目(windows)
:: author: houbb
:: LastUpdateTime:  2018-1-22 09:08:52
:: 用法：双击运行，或者当前路径 cmd 直接输入 release.bat

:: 关闭回显
@echo OFF

ECHO "============================= RELEASE START..."

:: 版本号信息(需要手动指定)
:::: 旧版本名称
SET version=0.0.10
:::: 新版本名称
SET newVersion=0.0.11
:::: 组织名称
SET groupName=com.github.houbb
:::: 项目名称
SET projectName=sensitive

:: release 项目版本
:::: snapshot 版本号
SET snapshot_version=%version%"-SNAPSHOT"
:::: 新的版本号
SET release_version=%version%

call mvn versions:set -DgroupId=%groupName% -DartifactId=%projectName% -DoldVersion=%snapshot_version% -DnewVersion=%release_version%
call mvn -N versions:update-child-modules
call mvn versions:commit
call echo "1. RELEASE %snapshot_version% TO %release_version% DONE."

:: 推送到 github
git add .
git commit -m "release branch %version%"
git push
git status

ECHO "2. PUSH TO GITHUB DONE."

:: 发布到 mvn 中央仓库
mvn clean deploy -P release

ECHO "3. PUSH TO MAVEN CENTER DONE."

:: 合并到 master 分支
:::: 分支名称
::SET branchName="release_"%version%
::git checkout master
::git pull
::git checkout %branchName%
::git rebase master
::git checkout master
::git merge %branchName%
::git push
::
::ECHO "3. MERGE TO MASTER DONE."


:::: 拉取新的分支
::SET newBranchName="release_"%newVersion%
::git branch %newBranchName%
::git checkout %newBranchName%
::git push --set-upstream origin %newBranchName%
::
::ECHO "4. NEW BRANCH DONE."
::
:::: 修改新分支的版本号
::SET snapshot_new_version=%newVersion%"-SNAPSHOT"
::call mvn versions:set -DgroupId=%groupName% -DartifactId=%projectName% -DoldVersion=%release_version% -DnewVersion=%snapshot_new_version%
::call mvn -N versions:update-child-modules
::call mvn versions:commit
::
::git add .
::git commit -m "modify branch %release_version% TO %snapshot_new_version%"
::git push
::git status
::ECHO "5. MODIFY %release_version% TO %snapshot_new_version% DONE."
::
::ECHO "============================= RELEASE END..."

