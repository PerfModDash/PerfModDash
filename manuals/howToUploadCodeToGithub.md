1. Create some temporary working directory, say temp_dir. Enter it.

2. Do the following:

git clone http://github.com/PerfModDash/PerfModDash.git
Cloning into 'PerfModDash'...
remote: Counting objects: 270, done.
remote: Compressing objects: 100% (159/159), done.
remote: Total 270 (delta 88), reused 269 (delta 87)
Receiving objects: 100% (270/270), 275.22 KiB | 74 KiB/s, done.
Resolving deltas: 100% (88/88), done.

It will download code to director PerfModDash

cd PerfModDash

You will see directories corresponding to data store (PsDataStore) and to other sub-projects. Create a directory for your subproject

mkdir PsGui

copy all your project structure (maybe without the target subdirectory which contains compiled code) to PsGui. The do


git add .

then


git commit -m "first commit of the PsGui project code"

finally:


git push origin master


You will be asked for your git userid and password. The output should look like:

Username for 'http://github.com': TomaszWlodek
Password for 'http://TomaszWlodek@github.com':
Counting objects: 159, done.
Delta compression using up to 2 threads.
Compressing objects: 100% (144/144), done.
Writing objects: 100% (158/158), 102.49 KiB, done.
Total 158 (delta 56), reused 0 (delta 0)
To http://github.com/PerfModDash/PerfModDash.git
