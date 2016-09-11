chalkboard([[hawaiian, HP1, HP2, HPP],
	    [marcoPolo, MP1, MP2, MPP],
	    [ninja, NP1, NP2, NPP],
	    [pepperoni, PP1, PP2, PPP],
	    [super, SP1, SP2, SPP]]):-
	permutation([HP1, MP1, NP1, PP1, SP1],
		    [ham, mussels, prawns, salami, tuna]),
	permutation([HP2, MP2, NP2, PP2, SP2],
		    [avocado, corn, olives, pineapple, tomato]),
	permutation([HPP, MPP, NPP, PPP, SPP],[5, 6, 7, 8, 9]),
	\+error(hawaiian, HP1, HP2, HPP),
	\+error(marcoPolo, MP1, MP2, MPP),
	\+error(ninja, NP1, NP2, NPP),
	\+error(pepperoni, PP1, PP2, PPP),
	\+error(super, SP1, SP2, SPP).

error(hawaiian, T1, _, P):- T1 \= mussels; P=<6.
error(marcoPolo, T1, T2, P):- T2 \= tomato; T1 == ham; P == 8.
error(_, ham, _, P):- P\=8.
error(_, T1, _, 8):- T1 \=ham.
error(pepperoni, _, _, P):- P \= 7.
error(super, _, pineapple, _).
error(_, tuna, corn, P):- P == 6.
error(_, tuna, T2, _):- T2 \= corn.
error(_, T1, corn, _):- T1 \= tuna.
error(_, _, olives, P):- P \= 5.
error(_, salami, olives, _).
error(_, _, pineapple, P):- P == 9.
