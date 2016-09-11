% Question 2

% Knowledge-base
gender(sue,f).
gender(jim,m).
gender(tom,m).
gender(joe,m).
gender(cami,f).
gender(bob,m).
gender(fay,f).
gender(beth,f).
hobby(sue,yoga).
hobby(sue,chess).
hobby(jim,chess).
hobby(tom,run).
hobby(tom,yoga).
hobby(joe,chess).
hobby(joe,run).
hobby(cami,yoga).
hobby(cami,chess).
hobby(cami,run).
hobby(bob,run).
hobby(fay,yoga).
hobby(fay,run).
hobby(fay,chess).
hobby(beth,chess).
hobby(beth,run).


%Rules
adjacent_gender(X,Y):- gender(X,Z), gender(Y,A), Z\=A.
adjacent_hobby(X,Y):- hobby(X,Z), hobby(Y,A), Z==A.

canSit([A,B,C,D,E,F,G,H]):- adjacent_gender(A,B), adjacent_gender(B,C),
adjacent_gender(C,D), adjacent_gender(D,E), adjacent_gender(E,F),
adjacent_gender(F,G), adjacent_gender(G,H), adjacent_gender(A,H),
adjacent_hobby(A,B), adjacent_hobby(B,C), adjacent_hobby(C,D),
adjacent_hobby(D,E), adjacent_hobby(E,F), adjacent_hobby(F,G),
adjacent_hobby(G,H), adjacent_hobby(A,H).

rotate(List, N, RotatedList) :-
	length(Back, N), append(Front, Back, List),
	append(Back, Front, RotatedList).

%rotatelist([],_).
%rotatelist([H|T],R):- append(T,[H],R).

names(X):- permutation([sue,jim,tom,bob,joe,cami,fay,beth],X).

seatingChart(X):- names(N), canSit(N), rotate(N,8,X).




