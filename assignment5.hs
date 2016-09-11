insert::(Ord a)=>[a]->a->[a]
insert [] num = [num]
insert (head:tail) num 
    | head < num = head:(insert tail num)
    | head == num = num:(head:tail)
    | head > num = num:(head:tail) 
    

insertSort::(Ord a)=>[a]->[a]
insertSort [] = []
insertSort (head:tail) = insert (insertSort tail) head

merge::(Ord a)=>[[a]]->[a]
merge [num] = num
merge (head:tail)
    | length(tail) == 0 = insertSort head
    | length(tail) > 0 = insertSort (merge tail ++ head)

center::(Ord a)=>[a]->Int->a->[a]
center list num filler
    | length(list) == num = list
    | length(list) < num = (lfill++list++rfill) where
       x = num - length(list)
       right = x `div` 2
       left = x - (x `div` 2)
       rfill = replicate right filler
       lfill = replicate left filler

largest::(Ord a)=>[a]->a
largest [num] = num
largest (head:x:tail)
    | head > x = max (largest tail) head
    | head < x = max (largest tail) x