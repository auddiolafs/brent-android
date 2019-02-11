# brent-android
Example of android project

### Að vinna með branches á git

#### 1. Búa til nýtt branch
```
git checkout -b nafn-a-branchi
```
(sleppa -b ef farið er inn á branch sem er þegar búið að búa til)

#### 2. Þegar maður er tilbúinn að pusha á branchið sitt
```
git add .
git commit -m "Sutt message um hvað var gert"
git pull origin master -> laga merge conflict ef það er til staðar
git push origin nafn-a-branchi
```
#### 3. Þegar branch er tilbúið til að fara á master
Framkvæma skref 2 og svo fara inn á github repo-ið, inn á pull requests og velja þar 'New pull request' til að merge-a branchinu á master.
Best er svo ef einhver annar í teyminu fer yfir kóðann/prófar áður en ýtt er á merge við master. Það er ekki 100% nauðsynlegt en það eru mjög góð vinnubrögð til að koma sem mest í veg fyrir að master brotni óvart.
